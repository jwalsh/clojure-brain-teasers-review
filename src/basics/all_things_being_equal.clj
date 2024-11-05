(ns basics.all-things-being-equal
  "Demonstrates equality semantics in Clojure"
  (:require [clojure.test :as t]))

;; Core functions demonstrating equality behavior
(defn strict-equality 
  "Demonstrates how = checks both value and type"
  []
  (= 1 1N 1.0 1.0M))

(defn numeric-equality
  "Demonstrates how == promotes numeric types before comparison"
  []
  (== 1 1N 1.0 1.0M))

(comment
  ;; Test with CIDER:
  (= 1 1N 1.0 1.0M)  ;; => false
  (== 1 1N 1.0 1.0M) ;; => true
  
  ;; Examine individual comparisons
  (= 1 1N)     ;; different types
  (= 1 1.0)    ;; different types
  (= 1.0 1.0M) ;; different types

  ;; Run tests inline
  (t/run-tests)
)
