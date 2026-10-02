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

;; -- Teaser 5: binding across await ----------------------------------
;; `binding` compiles to: set the var, try the body, finally restore it.
;; An ^:async function compiles to a JavaScript async function, and an
;; `await` inside a try block suspends the function without running the
;; finally clause. So the binding is still in place when the function
;; resumes.
;;
;; The surprise is the other direction. ClojureScript has one thread and a
;; dynamic var is a single global slot. While the function is suspended,
;; the slot still holds the bound value, and every other piece of code the
;; event loop runs in the meantime sees it.

(def ^:dynamic *ctx* nil)

(defn ^:async binding-across-await
  "Reads *ctx* after an await inside a binding. Returns :active: the
   binding is restored only when the body finishes, and it has not."
  []
  (binding [*ctx* :active]
    (await (slow-promise 20))
    *ctx*))

(defn ^:async binding-leaks-during-await
  "Starts `binding-across-await`, and while it is suspended reads *ctx*
   from code that is outside the binding form. Returns what that code saw
   during the suspension, and what it sees once the function has finished.

   Returns {:during :active, :after nil}: the binding leaked to an
   unrelated reader for as long as the function was suspended."
  []
  (let [suspended (binding-across-await)
        during (await (js/Promise. (fn [resolve]
                                     (js/setTimeout #(resolve *ctx*) 5))))]
    (await suspended)
    {:during during :after *ctx*}))
