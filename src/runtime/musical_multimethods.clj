(ns runtime.musical-multimethods
  "Demonstrates multimethods using musical transformations as examples"
  (:require [clojure.test :as t]))

;; Define the subject as a vector of notes
(def subject ["A" "D" "E" "A"])

;; Define a generic procedure with multimethod dispatch
;; The transformation parameter determines which method is used
(defmulti play 
  "Plays a musical pattern using different transformations"
  (fn [pattern transformation & args] transformation))

;; Default method - returns subject unmodified
(defmethod play :default [_ _] 
  subject)

;; Reverses subject
(defmethod play :retrograde [_ _] 
  (reverse subject))

;; Changes the key to minor
(defmethod play :transpose-minor [_ _] 
  (mapv #(str % "m") subject))

;; Creates crab canon (musical palindrome pattern)
(defmethod play :mobius [_ _]
  (let [half (quot (count subject) 2)       ;; find half of the subject
        first-half (subvec subject 0 half)  ;; create vector of the first half
        second-half (reverse (subvec subject half))] ;; create vector of second half, reverse
    (vec (concat first-half second-half first-half)))) ;; combine into continuous mobius strip

;; Additional methods to demonstrate multimethod principles

(defmethod play :transpose [_ _ interval]
  (let [notes ["A" "B" "C" "D" "E" "F" "G"]
        note->index (zipmap notes (range))
        transpose-note (fn [note]
                         (let [idx (note->index note)
                               new-idx (mod (+ idx interval) (count notes))]
                           (nth notes new-idx)))]
    (mapv transpose-note subject)))

(defmethod play :augmentation [_ _ repeats]
  (vec (mapcat #(repeat repeats %) subject)))

(defmethod play :inversion [_ _]
  (let [notes ["A" "B" "C" "D" "E" "F" "G"]
        note->index (zipmap notes (range))
        invert-note (fn [note]
                      (let [idx (note->index note)
                            new-idx (mod (- (count notes) 1 idx) (count notes))]
                        (nth notes new-idx)))]
    (mapv invert-note subject)))

;; Methods can also be added dynamically at runtime
(defmethod play :echo [_ _ volume-levels]
  (vec (mapcat #(map (fn [level] (str % level)) volume-levels) subject)))

;; Removing methods
(comment
  ;; To remove a method:
  (remove-method play :echo)
  
  ;; To check available methods:
  (methods play))

;; Multimethod hierarchy example
(derive ::jazz ::music)
(derive ::bebop ::jazz)
(derive ::classical ::music)
(derive ::baroque ::classical)

(defmethod play ::music [_ _]
  ["Do" "Re" "Mi" "Fa"])

(defmethod play ::jazz [_ _]
  ["Bb7" "Eb7" "Ab7" "Db7"])

(defmethod play ::bebop [_ _]
  ["Cmaj7" "A7" "Dm7" "G7"])

(comment
  ;; REPL experiments
  
  ;; Basic pattern transformations
  (play "original" :default)
  ;; => ["A" "D" "E" "A"]
  
  (play "backwards" :retrograde)
  ;; => ["A" "E" "D" "A"]
  
  (play "to-minor-key" :transpose-minor)
  ;; => ["Am" "Dm" "Em" "Am"]
  
  (play "crab-canon" :mobius)
  ;; => ["A" "D" "A" "E" "A" "D"]
  
  ;; Advanced transformations with additional parameters
  (play "up-a-fifth" :transpose 4)
  ;; => ["E" "A" "B" "E"]
  
  (play "doubled" :augmentation 2)
  ;; => ["A" "A" "D" "D" "E" "E" "A" "A"]
  
  (play "inverted" :inversion)
  ;; => ["G" "D" "C" "G"]
  
  (play "with-echo" :echo ["" "p" "pp"])
  ;; => ["A" "Ap" "App" "D" "Dp" "Dpp" ...]
  
  ;; Hierarchy-based methods
  (play "music" ::music)
  ;; => ["Do" "Re" "Mi" "Fa"]
  
  (play "jazz" ::jazz)
  ;; => ["Bb7" "Eb7" "Ab7" "Db7"]
  
  (play "bebop" ::bebop)
  ;; => ["Cmaj7" "A7" "Dm7" "G7"]
  
  ;; Run tests inline
  (t/run-tests))