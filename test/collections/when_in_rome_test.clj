(ns collections.when-in-rome-test
  (:require [clojure.test :refer [deftest testing is]]
            [collections.when-in-rome :as sut]))

(deftest list-conj-test
  (testing "conj adds elements at beginning of list"
    (is (= '(:trevi-fountain :pantheon :colosseum :vatican)
           (conj '(:colosseum :vatican) :pantheon :trevi-fountain))
        "elements are added at the front of the list")
    (is (= (first (conj '(:a :b) :c :d)) :d)
        "last argument becomes first element")))

(deftest vector-conj-test
  (testing "conj adds elements at end of vector"
    (is (= [:colosseum :vatican :pantheon :trevi-fountain]
           (conj [:colosseum :vatican] :pantheon :trevi-fountain))
        "elements are added at the end of vector")
    (is (= (last (conj [:a :b] :c :d)) :d)
        "last argument becomes last element")))

(deftest retaining-order-test
  (testing "different ways to maintain order in transformations"
    (let [[lazy-result mapv-result into-result] (sut/test-retaining-order)]
      (testing "results are equal but different types"
        (is (= lazy-result mapv-result into-result)
            "all approaches produce same sequence of values")
        (is (instance? clojure.lang.LazySeq lazy-result)
            "map returns lazy sequence")
        (is (vector? mapv-result)
            "mapv returns vector")
        (is (vector? into-result)
            "into [] returns vector"))
      
      (testing "expected transformed values"
        (is (= '(3 4 5 6 7) lazy-result)
            "map transforms values lazily")
        (is (= [3 4 5 6 7] mapv-result)
            "mapv transforms values eagerly")
        (is (= [3 4 5 6 7] into-result)
            "into collects transformed values")))))

(deftest set-conj-test
  (testing "conj with sets handles duplicates and ordering"
    (let [[sight-set num-set] (sut/test-set-conj)]
      (testing "sight-seeing set"
        (is (= 4 (count sight-set))
            "all unique elements added")
        (is (every? sight-set [:colosseum :vatican :pantheon :trevi-fountain])
            "contains all sights regardless of order"))
      
      (testing "number set with duplicates"
        (is (= #{1 2 3 4} num-set)
            "duplicates are ignored")
        (is (= 4 (count num-set))
            "only unique values counted")))))
