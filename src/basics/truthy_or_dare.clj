(ns basics.truthy-or-dare
  "Demonstrates truthy/falsy value semantics in Clojure"
  (:require [clojure.test :as t]))

(defn test-true-predicate
  "Test true? predicate"
  []
  [(true? true)       ;; true - only true is true
   (true? :keyword)]) ;; false - even though truthy

(defn test-false-predicate
  "Test false? predicate"
  []
  [(false? false)     ;; true - only false is false
   (false? nil)       ;; false - even though falsy
   (false? '())       ;; false - empty list is truthy
   (false? 0)])       ;; false - numbers are truthy

(defn truthy-test
  "Test logical truthiness with if"
  []
  [(if :keyword "truthy" "falsy")     ;; keywords are truthy
   (if '() "truthy" "falsy")          ;; empty lists are truthy
   (if 0 "truthy" "falsy")])          ;; zero is truthy

(comment
  ;; REPL experiments
  (true? true)      ;; true
  (true? :keyword)  ;; false
  (true? "string")  ;; false
  
  (false? false)    ;; true
  (false? nil)      ;; false
  (false? 0)        ;; false
  
  ;; Only false and nil are logically false
  (if nil "truthy" "falsy")      ;; "falsy"
  (if false "truthy" "falsy")    ;; "falsy"
  (if :keyword "truthy" "falsy") ;; "truthy"
  
  ;; Run tests inline
  (t/run-tests))
