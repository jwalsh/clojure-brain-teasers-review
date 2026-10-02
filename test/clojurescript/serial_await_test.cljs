(ns clojurescript.serial-await-test
  "Tests for ClojureScript async/await brain teasers.
   Requires ClojureScript 1.12.145+ with ^:async support."
  (:require [cljs.test :refer [deftest testing is]]
            [clojurescript.serial-await :as sut]))

;; -- T1: Serial vs Parallel timing ------------------------------------

(deftest ^:async serial-await-is-slow
  (testing "let with two awaits runs sequentially, not in parallel"
    (let [start (.now js/Date)
          result (await (sut/fetch-two-serial))
          elapsed (- (.now js/Date) start)]
      (is (= [100 100] result)
          "both promises should resolve to their delay value")
      (is (>= elapsed 180)
          "serial awaits should take ~200ms (at least 180ms)")
      (is (<= elapsed 350)
          "should not take unreasonably long"))))

(deftest ^:async parallel-await-is-fast
  (testing "Promise.all launches both promises concurrently"
    (let [start (.now js/Date)
          result (await (sut/fetch-two-parallel))
          elapsed (- (.now js/Date) start)]
      (is (= [100 100] result)
          "both promises should resolve to their delay value")
      (is (<= elapsed 180)
          "parallel awaits should take ~100ms (under 180ms)"))))

;; -- T3: await is not a function value --------------------------------

(deftest ^:async await-all-uses-promise-all
  (testing "correct approach: Promise.all over a JS array of promises"
    (let [result (await (sut/await-all-broken
                         [(sut/slow-promise 10)
                          (sut/slow-promise 20)
                          (sut/slow-promise 30)]))]
      (is (= 3 (count (js->clj result)))
          "should resolve all three promises")
      (is (= [10 20 30] (js->clj result))
          "each promise resolves to its delay value"))))

;; -- T5: binding across await ----------------------------------------

(deftest ^:async binding-survives-await-inside-the-frame
  (testing "await inside binding suspends without running the finally"
    (let [result (await (sut/binding-across-await))]
      (is (= :active result)
          "*ctx* is still bound when the function resumes"))))

(deftest ^:async binding-leaks-to-other-code-during-await
  (testing "code outside the binding form sees the bound value while the function is suspended"
    (let [result (await (sut/binding-leaks-during-await))]
      (is (= :active (:during result))
          "an unrelated reader sees the binding during the suspension")
      (is (nil? (:after result))
          "the var is restored once the function has finished"))))
