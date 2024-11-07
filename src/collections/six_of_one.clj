(ns collections.six-of-one
  "Demonstrates Clojure's collection equality semantics")

(defn test-sequential-equality
  "Tests equality of different sequential collections with same elements"
  []
  [(= [0 1 2 3 4 5]        ;; vector literal
      '(0 1 2 3 4 5)        ;; list literal
      (range 6))])          ;; sequence from range

(defn test-different-types
  "Tests equality between different collection types"
  []
  [;; Sequential collections and sets are never equal
   (= [0 1 2] #{0 1 2})    ;; false
   ;; Empty collections of different types are never equal
   (= [] #{})              ;; false
   ;; Java collections compare by contents too
   (= [1 2 3]
      (java.util.ArrayList. [1 2 3]))])  ;; true
