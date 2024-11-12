(ns runtime.abracordabra-test
  (:require [clojure.test :refer :all]
            [runtime.abracordabra :refer :all]))

(deftest test-flip-map
  (testing "Basic flip map behavior"
    (is (= :tail (flip :head)) "Should flip head to tail")
    (is (= :head (flip :tail)) "Should flip tail to head")))

(deftest test-destructuring-with-or
  (testing "Order dependency in destructuring with :or"
    ;; Execute in let to match source example
    (let [empty {}
          left nil, right nil
          {:keys [left right] :or {left :head, right (flip left)}} empty
          result-1 right
          
          ;; Reset and try other order
          left nil, right nil
          {:keys [right left] :or {left :head, right (flip left)}} empty
          result-2 right]
      (is (= :tail result-1) "First order should yield :tail")
      (is (nil? result-2) "Second order should yield nil")
      (is (not= result-1 result-2) "Results should differ based on order"))))

(deftest test-safe-approach
  (testing "Safe approach with explicit steps"
    (let [empty {}
          {:keys [left right] :or {left :head}} empty
          right (or right (flip left))]
      (is (= :tail right) "Safe approach should always yield :tail"))))

(deftest test-basic-defaults
  (testing "Basic :or usage with simple values"
    (let [{:keys [a b] :or {a 1 b 2}} {}]
      (is (= [1 2] [a b]) "Should use default values"))))

(deftest test-computed-defaults
  (testing "Computed defaults behavior"
    (let [{:keys [x y] :or {x 0 y (inc x)}} {}]
      (is (= [0 1] [x y]) "Should compute y from x's default"))))