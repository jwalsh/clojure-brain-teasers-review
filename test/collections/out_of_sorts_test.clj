(ns collections.out-of-sorts-test
  (:require [clojure.test :refer [deftest testing is are]]
            [collections.out-of-sorts :as sut]))

(deftest comparator-casting-test
  (testing "Keyword used as index in sorted set"
    (is (thrown? ClassCastException (first (sut/compararator-cast-test)))
        "Using keyword as index in sorted set of numbers throws ClassCastException")))

(deftest numeric-comparator-test
  (testing "Java-style numeric comparator"
    (let [[equal less greater sorted-result] (sut/num-comparator-test)]
      (testing "basic comparisons"
        (is (zero? equal) "Equal numbers return 0")
        (is (neg? less) "First less than second returns negative")
        (is (pos? greater) "First greater than second returns positive"))
      
      (testing "sorted set behavior"
        (is (= #{1 2 3 4 5} sorted-result)
            "Numbers are properly ordered")))))

(deftest boolean-comparator-test
  (testing "Boolean-style comparator"
    (let [[result] (sut/num-comparator-bool-test)]
      (is (= #{1 2 3 4 5} result)
          "Boolean comparator correctly orders numbers"))))

(deftest default-comparator-test
  (testing "Default comparator behavior"
    (let [[kw-compare num-compare] (sut/default-comparator-test)]
      (testing "same-type comparisons"
        (is (neg? kw-compare) "Keywords can be compared")
        (is (neg? num-compare) "Numbers can be compared"))
      
      (testing "cross-type comparison failure"
        (is (thrown? ClassCastException (compare 1 :a))
            "Comparing different types throws ClassCastException")))))

(deftest custom-string-comparator-test
  (testing "String-based total ordering comparator"
    (let [[result] (sut/custom-comparator-test)
          elements (vec result)]
      (is (= 6 (count result))
          "All elements are included")
      (is (= [1 10 2 :hello false "x"] elements)
          "Elements are ordered by string representation"))))
