(ns basics.youve-been-promoted
  "Demonstrates arithmetic overflow and promotion behavior in Clojure"
  (:require [clojure.test :as t]))

(def bignum 9223372036854775807)   ;; 2^63 - 1 (max long)
(def biggernum 9223372036854775808N) ;; 2^63 needs N suffix for BigInt

(defn arithmetic-test-1 
  "Test standard arithmetic that checks for overflow"
  []
  (= biggernum (+ bignum 1)))

(defn arithmetic-test-2
  "Test promoting arithmetic that handles overflow"
  []
  (= biggernum (+' bignum 1)))

(defn arithmetic-test-3
  "Test unchecked arithmetic that allows overflow"
  []
  (= biggernum (unchecked-add bignum 1)))

(comment
  ;; REPL experiments
  (+ bignum 1)  ;; ArithmeticException
  (+' bignum 1) ;; 9223372036854775808N - promotes to BigInt
  (unchecked-add bignum 1) ;; -9223372036854775808 - overflows silently
  
  ;; Run tests inline
  (t/run-tests))
