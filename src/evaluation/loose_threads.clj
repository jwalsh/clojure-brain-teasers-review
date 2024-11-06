(ns evaluation.loose-threads
  (:require [clojure.string :as str]))

(= (-> 10 inc)
   (-> 10 (+ 1)))

(* (- (inc 100) 80) 2)

(-> 100
    inc
    (- 80)
    (* 2))

(as-> 10 $ (+ $ 1))

(as-> "a:b:::c:e:" $
  (str/split $ #":")
  (remove str/blank? $)
  (str/join "-" $))
