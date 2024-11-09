(ns evaluation.loose-threads-test
  (:require [clojure.test :refer [deftest testing is]]
            [evaluation.loose-threads :as sut]
            [clojure.string :as str]))

(deftest thread-first-equivalence-test
  (testing "Thread-first macro equivalence"
    (is (= (-> 10 inc)
           (-> 10 (+ 1)))
        "inc and (+ 1) are equivalent under ->")
    
    (is (= 11 (-> 10 inc))
        "Threading evaluates to expected result")))

(deftest nested-vs-threaded-test
  (testing "Nested expression vs thread-first"
    (let [nested-result (* (- (inc 100) 80) 2)
          threaded-result (-> 100
                            inc
                            (- 80)
                            (* 2))]
      (is (= nested-result threaded-result)
          "Nested and threaded expressions give same result")
      (is (= 42 nested-result)
          "Both expressions evaluate to 42"))))

(deftest thread-as-test
  (testing "Thread-as macro with simple example"
    (is (= 11
           (as-> 10 $ (+ $ 1)))
        "Thread-as allows explicit naming of threaded value"))
  
  (testing "Thread-as with string processing"
    (is (= "a-b-c-e"
           (as-> "a:b:::c:e:" $
             (str/split $ #":")
             (remove str/blank? $)
             (str/join "-" $)))
        "Thread-as enables complex transformations with intermediate results")))
