(ns collections.needle-in-a-haystack-test
  (:require [clojure.test :refer :all]
            [collections.needle-in-a-haystack :refer :all]))

;; Testing haystack vector behavior
(deftest haystack-structure-test
  (testing "Haystack vector properties"
    (is (vector? haystack) "Haystack should be a vector")
    (is (= 101 (count haystack)) "Should contain 100 numbers plus :needle")
    (is (some #{:needle} haystack) "Should contain :needle")
    (is (every? number? (remove #{:needle} haystack)) "All non-needle elements should be numbers")
    (is (= (sort (remove #{:needle} haystack)) (range 100)) "Should contain numbers 0-99")))

;; Testing contains? behavior
(deftest contains-vector-behavior-test
  (testing "Contains? with vectors - index-based lookup"
    (let [results (contains-test)]
      (is (= 2 (count results)) "Should return two results")
      (is (false? (first results)) "Vector contains? should return false for value :needle")
      (is (true? (second results)) "Vector contains? should return true for index 50"))))

;; Testing set behavior
(deftest haystack-set-structure-test
  (testing "Haystack set properties"
    (is (set? haystack-set) "Haystack-set should be a set")
    (is (= 101 (count haystack-set)) "Should contain 100 numbers plus :needle")
    (is (contains? haystack-set :needle) "Should contain :needle")
    (is (every? number? (disj haystack-set :needle)) "All non-needle elements should be numbers")
    (is (= (sort (disj haystack-set :needle)) (range 100)) "Should contain numbers 0-99")))

;; Testing indexed data structures
(deftest indexed-lookup-behavior-test
  (testing "Set containment with constant-time lookup"
    (let [results (indexed-data-structures-test)]
      (is (= 1 (count results)) "Should return one result")
      (is (true? (first results)) "Set should contain :needle")
      (is (= results [(contains? haystack-set :needle)]) "Should match direct contains? check"))))

;; Testing linear search implementations
(deftest linear-search-implementations-test
  (testing "Different linear search approaches"
    (let [results (linear-search-test)]
      (is (= 2 (count results)) "Should return two results")
      (is (= :needle (first results)) "Filter should find :needle")
      (is (= :needle (second results)) "Some should find :needle")
      (is (= results [(first (filter #{:needle} haystack-set))
                     (some #{:needle} haystack-set)]) "Should match direct filter/some checks"))))

;; Testing predicate behavior
(deftest predicate-function-test
  (testing "Predicate function behavior with odd?"
    (let [results (predicates-test)]
      (is (= 2 (count results)) "Should return two results")
      (is (= 1 (first results)) "First odd number should be 1")
      (is (true? (second results)) "Some odd? should return true")
      (is (= results [(first (filter odd? (range 10)))
                     (some odd? (range 10))]) "Should match direct filter/some checks")))
  
  (testing "Additional predicate properties"
    (is (= (take 5 (filter odd? (range 100)))
           [1 3 5 7 9]) "Should find first 5 odd numbers")
    (is (true? (some odd? [2 4 6 7 8])) "Should find odd number in middle")
    (is (nil? (some odd? [2 4 6 8 10])) "Should return nil when no odds found")))

;; Performance considerations test
(deftest performance-characteristics-test
  (testing "Set vs Vector lookup performance"
    (let [large-vec (vec (range 1000000))
          large-set (set large-vec)]
      (testing "Set lookup should be constant time"
        (is (contains? large-set 999999) "Should find last element quickly"))
      (testing "Vector lookup should be constant time for valid indices"
        (is (contains? large-vec 999999) "Should find last index quickly")))))

;; Edge cases test
(deftest edge-cases-test
  (testing "Empty collections"
    (is (nil? (first (filter #{:needle} #{}))) "Filter on empty set returns nil")
    (is (nil? (some #{:needle} #{})) "Some on empty set returns nil"))
  (testing "Single element collections"
    (is (= :needle (first (filter #{:needle} #{:needle}))) "Filter finds single element")
    (is (= :needle (some #{:needle} #{:needle})) "Some finds single element"))
  (testing "Multiple identical elements"
    (let [vec-with-dupes (vec (concat [1 1 1] (range 2 100) [:needle :needle]))]
      (is (= 1 (count (set (filter #{:needle} vec-with-dupes)))) "Should handle duplicate needles"))))