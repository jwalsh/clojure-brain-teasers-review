(ns runtime.boom-goes-the-dynamite
  (:require [clojure.string :as str]))

;; Original implementation with stack overflow risk
(defn doubled
  "Returns a sequence of all i's from 0 to n (exclusive), doubled.
   Ex: (doubled 3) ;; (0 0 1 1 2 2)"
  [n]
  (loop [i 0
         output ()]
    (if (< i n)
      (recur (inc i)
             (concat output [i i]))
      output)))

;; Test cases for doubled
(doubled 3)
;;=> (0 0 1 1 2 2)

;; This will cause stack overflow
(= 0 (first (doubled 50000)))
;; Execution error (StackOverflowError)

;; Simplified concat implementation to demonstrate lazy-seq
(defn concat1
  "Simplified implementation of concat using lazy-seq"
  ([] (lazy-seq ()))  ;; Return empty sequence
  ([coll] (lazy-seq coll))
  ([coll1 coll2]
   (lazy-seq
    (let [s (seq coll1)]
      (if s
        (cons (first s) (concat1 (rest s) coll2))
        coll2)))))

;; Test cases for concat1
(concat1 [])
;;=> ()

(concat1 [1 2 3])
;;=> (1 2 3)

(concat1 [1 2 3] [4 5 6])
;;=> (1 2 3 4 5 6)

;; Improved implementation that avoids stack overflow
(defn doubled-take2
  "Returns a vector of all i's from 0 to n (exclusive), doubled.
   Uses conj instead of concat to avoid stack overflow.
   Ex: (doubled-take2 3) ;; [0 0 1 1 2 2]"
  [n]
  (loop [i 0
         output []]
    (if (< i n)
      (recur (inc i) (conj output i i))
      output)))

;; Test cases for doubled-take2
(doubled-take2 3)
;;=> [0 0 1 1 2 2]

;; This works fine with large numbers
(first (doubled-take2 50000))
;;=> 0

;; Additional examples showing the difference in performance
(time (doubled 10))
;;=> "Elapsed time: 0.1234 msecs"
;;=> (0 0 1 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9)

(time (doubled-take2 10))
;;=> "Elapsed time: 0.0234 msecs"
;;=> [0 0 1 1 2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9]

;; Example showing memory usage difference
(defn memory-test []
  (println "Testing doubled-take2 with large n:")
  (let [result (doubled-take2 100000)]
    (println "Length:" (count result))
    (println "First few elements:" (take 6 result)))

  (println "\nTesting doubled with small n (large n would overflow):")
  (let [result (doubled 100)]
    (println "Length:" (count result))
    (println "First few elements:" (take 6 result))))

;; Run memory test
(memory-test)
;;=> Testing doubled-take2 with large n:
;;=> Length: 200000
;;=> First few elements: (0 0 1 1 2 2)
;;=> 
;;=> Testing doubled with small n (large n would overflow):
;;=> Length: 200
;;=> First few elements: (0 0 1 1 2 2)
