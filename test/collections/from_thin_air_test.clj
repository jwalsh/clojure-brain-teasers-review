(ns collections.from-thin-air-test
  (:require [clojure.test :refer [deftest testing is]]
            [collections.from-thin-air :as sut]))

(deftest test-assoc-nil-test
  (testing "assoc creates map from nil"
    (let [[nil-test equality-test] (sut/test-assoc-nil)]
      (is (false? nil-test)
          "assoc returns a new map, not nil")
      (is (true? equality-test)
          "map contains expected key/value"))))

(deftest test-nil-as-empty-test
  (testing "nil acts as empty collection"
    (let [[cnt nth1 nth2 get-val contains-val first-val rest-val] 
          (sut/test-nil-as-empty)]
      (is (zero? cnt) "count of nil is 0")
      (is (nil? nth1) "nth returns nil for any index")
      (is (nil? nth2) "nth returns nil even with count")
      (is (nil? get-val) "get returns nil")
      (is (false? contains-val) "contains? returns false")
      (is (nil? first-val) "first returns nil")
      (is (= '() rest-val) "rest returns empty list"))))

(deftest test-conj-nil-test
  (testing "conj creates vector from nil"
    (let [[nil-test equality-test] (sut/test-conj-nil)]
      (is (false? nil-test)
          "conj returns a new vector, not nil")
      (is (true? equality-test)
          "vector contains expected element"))))

(deftest test-java-interop-test
  (testing "Java interop throws on nil"
    (is (thrown? NullPointerException
                 (first (sut/test-java-interop)))
        "string operations throw on nil")))
