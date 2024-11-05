(ns basics.ba-nan-a-peel-test
  "Tests for NaN equality behavior in Clojure"
  (:require [clojure.test :refer [deftest is testing]]
            [basics.ba-nan-a-peel :as sut]))

(deftest nan-behaviors
  (testing "NaN equality semantics"
    (testing "using ="
      (is (false? (sut/nan-equality))
          "NaN should not equal itself with ="))

    (testing "using =="
      (is (false? (sut/nan-numeric-equality))
          "NaN should not equal itself even with =="))

    (testing "as map key"
      (let [[count-val get-val] (sut/nan-as-key)]
        (is (= 1 count-val)
            "Map should contain the NaN key entry")
        (is (nil? get-val)
            "NaN key lookup should return nil")))

    (testing "NaN from operations"
      (is (Double/isNaN (/ 0.0 0.0))
          "Dividing 0.0 by 0.0 should produce NaN")
      (is (Double/isNaN (Math/sqrt -1.0))
          "Square root of negative number should produce NaN"))))
