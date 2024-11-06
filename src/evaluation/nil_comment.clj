(ns evaluation.nil-comment)

(defn nil-coment-test []
  [(map inc [1 2 3 (comment 4) 5])])

(defn my-function [x] x)

(comment
  (my-function 100))


