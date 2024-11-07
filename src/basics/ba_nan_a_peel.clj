(ns basics.ba-nan-a-peel
  "Demonstrates special floating point equality behavior with NaN"
  (:require [clojure.test :as t]))

(defn nan-equality
  "Test direct NaN equality"
  []
  (= ##NaN ##NaN))

(defn nan-numeric-equality
  "Test numeric NaN equality"
  []
  (== ##NaN ##NaN))

(defn nan-as-key
  "Demonstrate NaN behavior in maps"
  []
  (let [m {##NaN 100}]
    [(count m)        ;; map has one entry
     (get m ##NaN)])) ;; but can't retrieve it

(comment
  ;; REPL experiments
  (= ##NaN ##NaN)   ;; false - NaN never equals itself
  (== ##NaN ##NaN)  ;; false - even with numeric equality

  ;; NaN from calculations
  (/ 0.0 0.0)       ;; ##NaN
  (Math/sqrt -1.0)  ;; ##NaN

  ;; Map behavior
  (def m {##NaN 100})
  (count m)         ;; 1
  (get m ##NaN)     ;; nil - can't find the key

  ;; Run tests inline
  (t/run-tests))
