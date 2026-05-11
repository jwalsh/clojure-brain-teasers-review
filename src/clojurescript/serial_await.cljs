(ns clojurescript.serial-await
  "Brain Teaser: Serial vs Parallel await in let

   ClojureScript 1.12.145 introduced ^:async / await.
   A let with multiple await bindings *looks* parallel but runs serial.
   The let desugaring forces sequencing: each binding suspends the async
   frame before the next initializer evaluates.

   Source: https://clojurescript.org/news/2026-05-07-release")

;; -- Setup: a slow promise that resolves after `ms` milliseconds --------

(defn slow-promise
  "Returns a js/Promise that resolves to `ms` after `ms` milliseconds."
  [ms]
  (js/Promise. (fn [resolve] (js/setTimeout #(resolve ms) ms))))

;; -- Teaser 1: Serial await in let --------------------------------------
;; Feels like two independent fetches should run concurrently (~100 ms),
;; but the let desugaring forces each await to complete before the next
;; binding initializes.  Total wall time: ~200 ms.

(defn ^:async fetch-two-serial
  "Awaits two slow promises sequentially. Total time: ~200ms."
  []
  (let [a (await (slow-promise 100))
        b (await (slow-promise 100))]
    [a b]))

;; -- Fix: Parallel with Promise.all ------------------------------------
;; Launch both promises eagerly, then await the combined result.

(defn ^:async fetch-two-parallel
  "Awaits two slow promises in parallel. Total time: ~100ms."
  []
  (let [result (await (js/Promise.all #js [(slow-promise 100)
                                            (slow-promise 100)]))]
    [(aget result 0) (aget result 1)]))

;; -- Teaser 3: await is not a function ---------------------------------
;; await is a special form.  Passing it to map either fails to compile
;; or treats `await` as a bare var lookup.  Even if it compiled, map is
;; lazy, so realization order would be undefined relative to the async
;; frame.

(defn ^:async await-all-broken
  "BROKEN: await is a special form, not a function value.
   (map await ps) does not do what you think."
  [ps]
  ;; This will either fail to compile or silently do the wrong thing:
  ;; (map await ps)
  ;;
  ;; Correct approach:
  (await (js/Promise.all (clj->js ps))))

;; -- Teaser 4: Async test that passes by accident ----------------------
;; Without ^:async deftest, a returned promise is never awaited.
;; The assertion fires asynchronously after the runner already reports
;; success.  The test "passes" but is lying.

;; BAD -- test runner never waits on the promise:
;; (deftest broken
;;   (-> (fetch-two-serial)
;;       (.then #(is (= [100 100] %)))))

;; GOOD -- use ^:async deftest so the runner awaits the result:
;; (deftest ^:async correct
;;   (is (= [100 100] (await (fetch-two-serial)))))

;; -- Teaser 5: binding does not survive await --------------------------
;; Dynamic bindings are implemented as try/finally push-pop.  The async
;; desugar splits the body across event-loop ticks, so the binding is
;; popped before the code after `await` runs.

(def ^:dynamic *ctx* nil)

(defn ^:async binding-across-await
  "Demonstrates that dynamic bindings are lost across await boundaries.
   Returns nil, not :active -- the binding is popped when the async
   frame suspends at await."
  []
  (binding [*ctx* :active]
    (await (slow-promise 10))
    *ctx*))
