(ns basics.youve-been-promoted
  "Demonstrates arithmetic overflow and promotion behavior in Clojure"
  (:require [clojure.test :as t]))

;; Constants for testing overflow
(def max-long    9223372036854775807)    ;; 2^63 - 1 (Long/MAX_VALUE)
(def min-long   -9223372036854775808)    ;; -2^63 (Long/MIN_VALUE)
(def big-n-max   9223372036854775807N)   ;; Same value but in BigInt format (with N suffix)
(def big-n-next  9223372036854775808N)   ;; 2^63 as BigInt
(def big-n-prev  9223372036854775806N)   ;; MAX_VALUE - 1 as BigInt

;; Regular arithmetic operations
(defn arithmetic-test-1
  "Test standard arithmetic that checks for overflow"
  []
  (= big-n-next (+ max-long 1)))

;; Promotion arithmetic operations
(defn arithmetic-test-2
  "Test promoting arithmetic that handles overflow"
  []
  (= big-n-next (+' max-long 1)))

;; Unchecked arithmetic operations
(defn arithmetic-test-3
  "Test unchecked arithmetic that allows overflow"
  []
  (= big-n-next (unchecked-add max-long 1)))

;; Additional promotion examples

(defn multiply-promotion
  "Demonstrates how multiplication can also overflow and be promoted"
  []
  ;; Can't return ArithmeticException directly, so handle in test
  [(*' max-long 2)         ;; 18446744073709551614N (BigInt)
   (unchecked-multiply max-long 2)])  ;; -2

(defn mixed-type-operations
  "Shows type promotion with mixed numeric types"
  []
  [(type (+ 1 2))           ;; Long
   (type (+ 1 2.0))         ;; Double (floating point)
   (type (+ 1 2/3))         ;; Ratio
   (type (+ 1 2N))          ;; BigInt
   (type (+ 1.0 2N))        ;; Double
   (type (+ 1/3 2N))])      ;; Ratio

(defn explicit-promotion
  "Shows explicit type promotion"
  []
  [(bigint 42)              ;; Convert to BigInt
   (bigdec 42)              ;; Convert to BigDecimal
   (rationalize 0.5)        ;; Convert to Ratio
   (double 42)])            ;; Convert to Double

(defn numerical-edge-cases
  "Shows edge cases in Clojure's numeric system"
  []
  [(= Double/POSITIVE_INFINITY (/ 1.0 0.0))   ;; Infinity
   (= Double/NEGATIVE_INFINITY (/ -1.0 0.0))  ;; Negative infinity
   (= Double/NaN (/ 0.0 0.0))                ;; Not a number
   (== Double/NaN Double/NaN)                ;; NaN never equals itself
   (= 3 3.0)                                 ;; Different types, not equal with =
   (== 3 3.0)])                              ;; Numeric equality with ==

(comment
  ;; REPL experiments showing overflow behavior
  (try (+ max-long 1) (catch Exception e (class e))) ;; ArithmeticException
  (+' max-long 1)                ;; 9223372036854775808N - promotes to BigInt
  (unchecked-add max-long 1)     ;; -9223372036854775808 - overflows silently to MIN_VALUE

  ;; Test that unchecked overflow wraps around
  (= min-long (unchecked-add max-long 1)) ;; true - wraps to minimum value
  
  ;; Comparing different numeric types
  (= 1 1.0)      ;; false - different types
  (== 1 1.0)     ;; true - same numeric value
  (= 1 1N)       ;; false - different types
  (== 1 1N)      ;; true - same numeric value
  (= 1/2 0.5)    ;; false - different types
  (== 1/2 0.5)   ;; true - same numeric value
  
  ;; BigInt examples (N suffix)
  (def a 999999999999999999999999999999N)
  (+ a 1)        ;; 1000000000000000000000000000000N - no overflow

  ;; Run tests inline
  (t/run-tests))
