(ns runtime.build-nest-test
  (:require [clojure.test :refer [deftest testing is]]
            [runtime.build-nest :as sut]))

(deftest for-comprehension-test
  (testing "single vs nested for behavior"
    (testing "single for with multiple bindings"
      (is (= '(0 0 0 3 4 5 6 8 10) sut/ex1)
          "generates flattened sequence"))

    (testing "nested for expressions"
      (is (= '((0 0 0) (3 4 5) (6 8 10)) sut/ex2)
          "generates nested sequences"))

    (testing "sequence inequality"
      (is (not= sut/ex1 sut/ex2)
          "nested and flattened sequences are not equal")))

  (testing "cartesian product behavior"
    (let [expected [[1 "a"] [1 "b"] [2 "a"] [2 "b"]]]
      (is (= expected
             (for [x [1 2] y ["a" "b"]]
               [x y]))
          "single for with multiple bindings produces cartesian product")))

  (testing "lazy evaluation"
    (is (= [0 0 0 0 0]
           (take 5 sut/large-computation))
        "large computation is lazy and only evaluates needed values")))
