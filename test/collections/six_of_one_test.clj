(ns collections.six-of-one-test
  (:require [clojure.test :refer [deftest testing is]]
            [collections.six-of-one :as sut]))

(deftest sequential-equality-test
  (testing "different sequential collections with same elements are equal"
    (is (= [0 1 2 3 4 5] 
           '(0 1 2 3 4 5) 
           (range 6))
        "vector = list = range sequence")
    
    (is (= (type [0 1 2]) clojure.lang.PersistentVector)
        "but they have different concrete types")
    (is (= (type '(0 1 2)) clojure.lang.PersistentList)
        "each with their own implementation")
    
    (is (= [1 [2 3] 4] 
           '(1 (2 3) 4))
        "equality works recursively for nested collections")))

(deftest collection-type-equality-test
  (testing "collections of different types are never equal"
    (is (not= [0 1 2] #{0 1 2})
        "sequences and sets are not equal")
    (is (not= [] #{})
        "even when empty")
    
    (testing "but Java collections compare equal by contents"
      (let [al (doto (java.util.ArrayList.)
                (.add 1)
                (.add 2)
                (.add 3))]
        (is (= [1 2 3] al)
            "ArrayList equals vector with same elements")))))

(deftest equality-partition-test
  (testing "collections are partitioned into equality types"
    (testing "sequential collections"
      (is (= [1 2 3] '(1 2 3))
          "vectors and lists")
      (is (= [1 2 3] (seq [1 2 3]))
          "vectors and seqs"))
    
    (testing "set collections" 
      (is (= #{1 2 3} (hash-set 1 2 3))
          "set literals and set functions")
      (is (= #{1 2 3} (java.util.HashSet. [1 2 3]))
          "Clojure and Java sets"))
    
    (testing "map collections"
      (is (= {:a 1 :b 2} 
             (hash-map :a 1 :b 2))
          "map literals and map functions")
      (is (= {:a 1 :b 2}
             (doto (java.util.HashMap.)
               (.put :a 1)
               (.put :b 2)))
          "Clojure and Java maps"))))
