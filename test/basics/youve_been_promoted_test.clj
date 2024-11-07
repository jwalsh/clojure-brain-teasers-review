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
        "should silently overflow to Long/MIN_VALUE")))
