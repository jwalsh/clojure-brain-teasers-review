(ns destructuring.one-is-the-loneliest-number-test
  (:require [clojure.test :refer [deftest is testing]]
            [destructuring.one-is-the-loneliest-number :as sut]))

(deftest selector-result-shape
  (testing "two directives give a map keyed by directive"
    (is (= {:select {:id 7} :excess {:lines [{:sku "A" :bin 9}]}}
           (sut/two-directives))))
  (testing "one directive gives the bare value"
    (is (= {:lines [{:sku "A" :bin 9}]} (sut/one-directive))))
  (testing "so looking up the directive's name in it finds nothing"
    (is (nil? (sut/the-bug-this-causes)))))

(deftest excess-and-vectors
  (testing "an extra key inside a vector of maps is not reported"
    (is (nil? (sut/excess-stops-at-vectors))))
  (testing "a selector mapped over the vector reports it"
    (is (= [{:bin 9}] (sut/one-form-per-line)))))
