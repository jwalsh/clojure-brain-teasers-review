(ns evaluation.quoth-test
  (:require [clojure.test :refer :all]
            [evaluation.quoth :refer :all]))

(deftest syntax-quote-tests-test
  (testing "Syntax quote behavior"
    (let [results (syntax-quote-tests)]
      (is (= 4 (count results)) "Should have 4 test results")
      (is (every? true? results) "All tests should evaluate to true"))))