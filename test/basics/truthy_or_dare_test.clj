(ns basics.truthy-or-dare-test
  (:require [clojure.test :refer [deftest testing is]]
            [basics.truthy-or-dare :as sut]))

(deftest predicate-test
  (testing "true? and false? predicates"
    (is (true? true)
        "true? true => true")
    (is (not (true? :sky-is-blue))
        "true? :sky-is-blue => false")
    (is (false? false)
        "false? false => true")
    (is (not (false? nil))
        "false? nil => false")
    (is (not (false? '()))
        "false? '() => false")
    (is (not (false? 0))
        "false? 0 => false")))

(deftest logical-truthiness-test
  (testing "only false and nil are logically false" 
    (let [moms-birthday "April 20, 1969"]
      
      ;; Using when-not with nil? check
      (is (= "Happy Birthday Mom!!"
             (when-not (nil? moms-birthday)
               "Happy Birthday Mom!!"))
          "when-not with nil? is verbose")
      
      ;; Using when with some? check  
      (is (= "Happy Birthday Mom!!"
             (when (some? moms-birthday)
               "Happy Birthday Mom!!"))
          "when with some? is clearer")
      
      ;; Using when with direct truthiness
      (is (= "Happy Birthday Mom!!"
             (when moms-birthday
               "Happy Birthday Mom!!"))
          "using value directly as condition is idiomatic"))))(ns basics.truthy-or-dare-test
  (:require [clojure.test :refer [deftest testing is]]
            [basics.truthy-or-dare :as sut]))

(deftest predicate-test
  (testing "true? and false? predicates"
    (is (true? true)
        "true? true => true")
    (is (not (true? :sky-is-blue))
        "true? :sky-is-blue => false")
    (is (false? false)
        "false? false => true")
    (is (not (false? nil))
        "false? nil => false")
    (is (not (false? '()))
        "false? '() => false")
    (is (not (false? 0))
        "false? 0 => false")))

(deftest logical-truthiness-test
  (testing "only false and nil are logically false" 
    (let [moms-birthday "April 20, 1969"]
      
      ;; Using when-not with nil? check
      (is (= "Happy Birthday Mom!!"
             (when-not (nil? moms-birthday)
               "Happy Birthday Mom!!"))
          "when-not with nil? is verbose")
      
      ;; Using when with some? check  
      (is (= "Happy Birthday Mom!!"
             (when (some? moms-birthday)
               "Happy Birthday Mom!!"))
          "when with some? is clearer")
      
      ;; Using when with direct truthiness
      (is (= "Happy Birthday Mom!!"
             (when moms-birthday
               "Happy Birthday Mom!!"))
          "using value directly as condition is idiomatic"))))
