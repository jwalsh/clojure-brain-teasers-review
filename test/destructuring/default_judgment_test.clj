(ns destructuring.default-judgment-test
  (:require [clojure.test :refer [deftest is testing]]
            [destructuring.default-judgment :as sut]))

(deftest which-a-does-the-default-see
  (testing "the default sees the binding outside the form, not the form's own"
    (is (= [1 100] (sut/which-a))))
  (testing "a present key never uses its default"
    (is (= [1 2] (sut/default-not-needed))))
  (testing "present and nil is present"
    (is (nil? (sut/present-but-nil)))))
