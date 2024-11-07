(ns runtime.banana-splits
  (:require [clojure.string :as str]))

;; Example splits showing subtle differences
(str/split "banana" #"an")
;;=> ["b" "" "a"]

(str/split "banana" #"na")
;;=> ["ba"]

;; With limit parameter demonstrations
(str/split "banana" #"na" 2)
;;=> ["ba" "na"]

;; Using negative limit to keep trailing empty strings
(str/split "banana" #"na" -1)
;;=> ["ba" "" ""]

;; Split lines example 
(str/split-lines "a\nb\nc\n\n")
;;=> ["a" "b" "c"]

;; Modified split-lines that retains trailing newlines
(defn split-lines-trailing [s]
  (str/split s #"\n" -1))

(->> "a\nb\nc\n\n"
     split-lines-trailing
     (map str/upper-case)
     (str/join "\n"))
;;=> "A\nB\nC\n\n"
