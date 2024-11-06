(ns basics.truthy-or-dare
  "Demonstrates truthy/falsy value semantics in Clojure")

(defn test-true-predicate
  "Test true? predicate"
  []
  [(true? true)           ;; true - only true is true
   (true? :sky-is-blue)]) ;; false - even though truthy

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
  [(if :keyword "truthy" "falsy")  ;; keywords are truthy
   (if '() "truthy" "falsy")       ;; empty lists are truthy 
   (if 0 "truthy" "falsy")])       ;; zero is truthy
