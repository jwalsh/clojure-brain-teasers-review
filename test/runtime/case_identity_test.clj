(ns runtime.case-identity-test
  (:require [clojure.test :refer [deftest testing is]]
            [runtime.case-identity :as sut]))

(deftest basic-case-test
  (testing "simple case test"
    (is (= 1 1))))
