(ns basics.truthy-or-dare
  "Demonstrates truthy/falsy value semantics in Clojure"
  (:require [clojure.test :as t]))

(defn predicate-tests
  "Test true? and false? predicates"
  []
  [(true? true)           ;; true
   (true? :sky-is-blue)   ;; false
   (false? false)         ;; true
   (false? nil)          ;; false
   (false? '())          ;; false
   (false? 0)])          ;; false

(defn conditional-tests
  "Test truthiness in if expressions"
  []
  [(if :keyword "truthy" "falsy")  ;; keywords are truthy
   (if '() "truthy" "falsy")       ;; empty lists are truthy
   (if 0 "truthy" "falsy")])       ;; zero is truthy

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
