(ns basics.youve-been-promoted-test
  "Tests for arithmetic overflow and promotion behavior in Clojure"
  (:require [clojure.test :refer [deftest is testing]]
            [basics.youve-been-promoted :as sut]))

(deftest overflow-handling
  (testing "regular arithmetic (+)"
    (is (thrown-with-msg? ArithmeticException #"long overflow"
                          (sut/arithmetic-test-1))
        "should throw ArithmeticException with 'long overflow' message"))

  (testing "promoting arithmetic (+')"
    (is (true? (sut/arithmetic-test-2))
        "should automatically promote to BigInt when needed"))

  (testing "unchecked arithmetic"
    (is (false? (sut/arithmetic-test-3))
        "should silently overflow to Long/MIN_VALUE")
    (is (= sut/min-long (unchecked-add sut/max-long 1))
        "unchecked overflow should wrap around to MIN_VALUE")))

(deftest multiplication-promotion
  (testing "multiplication overflow handling"
    (is (thrown? ArithmeticException
                (* sut/max-long 2))
        "regular multiplication should throw on overflow")
    
    (is (= (*' sut/max-long 2)
           (first (sut/multiply-promotion)))
        "promoting multiplication should produce a BigInt")
    
    (is (= (unchecked-multiply sut/max-long 2)
           (second (sut/multiply-promotion)))
        "unchecked multiplication should silently overflow")))

(deftest numeric-type-tests
  (testing "mixed-type operations"
    (let [results (sut/mixed-type-operations)]
      (is (= Long (nth results 0)))
      (is (= Double (nth results 1)))
      (is (= clojure.lang.Ratio (nth results 2)))
      (is (= clojure.lang.BigInt (nth results 3)))
      (is (= Double (nth results 4)))
      (is (= clojure.lang.Ratio (nth results 5)))))
  
  (testing "explicit type promotion"
    (let [results (sut/explicit-promotion)]
      (is (= clojure.lang.BigInt (type (nth results 0))))
      (is (= java.math.BigDecimal (type (nth results 1))))
      (is (= clojure.lang.Ratio (type (nth results 2))))
      (is (= Double (type (nth results 3)))))))

(deftest edge-case-tests
  (testing "numeric edge cases"
    (let [results (sut/numerical-edge-cases)]
      (is (true? (nth results 0)) "Infinity equality check")
      (is (true? (nth results 1)) "Negative infinity equality check")
      (is (false? (nth results 2)) "NaN equality check with =")
      (is (false? (nth results 3)) "NaN equality check with ==")
      (is (false? (nth results 4)) "Different numeric types with =")
      (is (true? (nth results 5)) "Different numeric types with =="))))
