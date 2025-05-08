(ns evaluation.meat-data-test
  (:require [clojure.test :refer :all]
            [evaluation.meat-data :refer :all]))

(deftest metadata-basics-test
  (testing "Basic metadata assignment and access"
    (let [v (with-meta [1 2 3] {:type "vector"})]
      (is (= {:type "vector"} (meta v)))
      (is (= [1 2 3] v)))))

(deftest the-road-so-far-test
  (testing "Metadata in the-road-so-far function"
    (let [result (the-road-so-far)]
      (is (map? result))
      (is (contains? result :hunters))
      (is (contains? result :threat))
      (is (contains? result :date))
      (is (vector? (:threat result))))))

(deftest vision-metadata-test
  (testing "Metadata in visions function"
    (let [result (visions)]
      (is (map? result))
      (is (= 'the-colt (:weapon result)))
      (is (= 'baby (:car result)))
      (is (= "Ruby " (:demon result)))
      (is (= 666 (:episode result))))))

(deftest type-hints-test
  (testing "Type hints as metadata"
    ;; Define a function with type hints for testing
    (defn ^String annotated-function [^Long x ^java.util.Date y]
      (str x " " y))
    
    ;; Check that the function has the right return type hint
    (is (= java.lang.String (:tag (meta #'annotated-function))))
    
    ;; Test our type-hints-usage function
    (let [result (type-hints-usage)]
      (is (= String (:tag (:string-meta result))))
      (is (= Long (:tag (:long-meta result))))
      (is (= true (:private (:private-meta result)))))))

(deftest metadata-preservation-test
  (testing "Metadata preservation across operations"
    (let [v (with-meta [1 2 3] {:important true})]
      ;; In Clojure collections, metadata is preserved for some operations
      ;; Note this behavior is implementation-dependent
      (is (map? (meta (conj v 4))))
      
      ;; But can be explicitly specified
      (is (= {:important true} 
             (meta (with-meta (conj v 4) (meta v))))))))

(deftest song-test
  (testing "Song vector content"
    (is (vector? song))
    (is (string? (first song)))))

(deftest collections-metadata-test
  (testing "Metadata on different collection types"
    (let [result (metadata-on-collections)]
      (is (= {:type "vector" :immutable true} (:vector-meta result)))
      (is (= {:type "map"} (:map-meta result)))
      (is (= {:type "set"} (:set-meta result)))
      ;; Metadata is preserved in Clojure's vector implementation 
      ;; This behavior is implementation-dependent
      (is (map? (:conj-meta result)))
      (is (= {:type "vector" :immutable true} (:with-meta-preserved result))))))

(deftest symbol-metadata-test
  (testing "Metadata on symbols"
    (let [[symbol-meta evaled-meta] (metadata-on-symbols)]
      (is (= {:doc "This is a symbol" :deprecated true} symbol-meta))
      (is (nil? evaled-meta)))))