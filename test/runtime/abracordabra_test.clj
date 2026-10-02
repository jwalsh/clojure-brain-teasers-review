(ns runtime.abracordabra-test
  (:require [clojure.test :refer [deftest is testing]]
            [runtime.abracordabra :as sut]))

(deftest test-flip-map
  (testing "Basic flip map behavior"
    (is (= :tail (sut/flip :head)) "Should flip head to tail")
    (is (= :head (sut/flip :tail)) "Should flip tail to head")))

(deftest test-destructuring-with-or
  (testing "On 1.13 a default sees the outer binding, in either key order"
    (is (= [:head nil] (sut/left-then-right))
        "the book's :tail became nil: (flip left) sees the outer nil")
    (is (= [:head nil] (sut/right-then-left)))
    (is (false? (sut/order-matters?))
        "the order dependence the puzzle is about is gone")))

(deftest test-safe-approach
  (testing "Explicit steps give the same answer on every version"
    (is (= :tail (sut/safe-approach)))))

(deftest test-basic-defaults
  (testing "Basic :or usage with simple values"
    (is (= [1 2] (sut/basic-defaults)))))

(deftest test-computed-defaults
  (testing "A default that refers to a sibling no longer compiles"
    (is (thrown? clojure.lang.Compiler$CompilerException
                 (eval sut/sibling-default-form))))
  (testing "The stepwise version still works"
    (is (= [0 1] (sut/safe-computed-defaults)))))
