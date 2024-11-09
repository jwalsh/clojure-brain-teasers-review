(ns evaluation.roll-dice-test
  (:require [clojure.test :refer [deftest testing is]]
            [evaluation.roll-dice :as sut]))

(deftest duplicate-set-key-test
  (testing "Set deduplication behavior"
    (let [[random-compare base-set dupe-set random-set] (sut/duplicate-set-key-test)]
      
      (testing "random value comparison"
        (is (true? random-compare)
            "Same random values produce equal sets"))
      
      (testing "conj with duplicate"
        (is (= #{1 2} base-set)
            "Adding duplicate value via conj has no effect"))
      
      (testing "set uniqueness"
        (is (= #{1 2} dupe-set)
            "Sets maintain unique elements"))
      
      (testing "random value deduplication"
        (is (set? random-set)
            "Result is a set")
        (is (<= (count random-set) 2)
            "Set may have fewer elements than inputs due to deduplication")
        (is (every? #(<= 0 % 5) random-set)
            "All values are valid dice rolls (0-5)")))))
