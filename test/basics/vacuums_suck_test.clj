(ns basics.vacuums-suck-test
  (:require [clojure.test :refer [deftest testing is]]
            [basics.vacuums-suck :as sut]))

(deftest vacuous-truth-test
  (testing "empty and returns true (vacuous truth)"
    (is (true? (and))
        "and with no args returns true"))

  (testing "every? with empty collection returns true (vacuous truth)"
    (is (true? (every? odd? []))
        "every? with empty collection returns true"))

  (testing "empty or returns nil"
    (is (nil? (or))
        "or with no args returns nil")))

(deftest vacuous-truth-examples
  (testing "additional examples of vacuous truth"
    (is (every? pos? [])
        "every positive number in empty set")
    (is (every? neg? [])
        "every negative number in empty set")
    (is (not-any? odd? [])
        "no odd numbers in empty set")
    (is (not-any? even? [])
        "no even numbers in empty set")))
