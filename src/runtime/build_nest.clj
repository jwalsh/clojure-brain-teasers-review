(ns runtime.build-nest)

;; Example with single for generating flattened sequence
(def ex1
  (for [a [0 1 2]
        b [3 4 5]]
    (* a b)))
;;=> (0 0 0 3 4 5 6 8 10)

;; Example with nested for generating nested sequences 
(def ex2
  (for [a [0 1 2]]
    (for [b [3 4 5]]
      (* a b))))
;;=> ((0 0 0) (3 4 5) (6 8 10))

;; Demonstrate they are not equal
(= ex1 ex2)
;;=> false

;; Additional examples showing for behavior
;; Single for with multiple bindings processes in order
(for [x [1 2] y ["a" "b"]]
  [x y])
;;=> ([1 "a"] [1 "b"] [2 "a"] [2 "b"])

;; Nested for creates sequences of sequences
(for [x [1 2]]
  (for [y ["a" "b"]]
    [x y]))
;;=> (([1 "a"] [1 "b"]) ([2 "a"] [2 "b"]))

;; Demonstration of lazy evaluation
(def large-computation
  (for [x (range 1000)
        y (range 1000)]
    (* x y)))
;; No computation happens until values are needed

;; Only computes what's needed
(take 5 large-computation)
;;=> (0 0 0 0 0)
