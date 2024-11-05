(ns basics.all-things-being-equal-test
  "Tests for numeric equality behavior in Clojure"
  (:require [clojure.test :refer [deftest is testing]]
            [basics.all-things-being-equal :as sut]))

(deftest equality-comparisons
  (testing "strict equality with = operator"
    (is (false? (sut/strict-equality)) 
        "= should return false when comparing different numeric types"))
  
  (testing "numeric equality with == operator"
    (is (true? (sut/numeric-equality))
        "== should return true when comparing equivalent numeric values")))
