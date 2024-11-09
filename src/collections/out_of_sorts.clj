(ns collections.out-of-sorts)

(defn compararator-cast-test []
  "Demonstrates type error when attempting to use a keyword as an index 
   in a sorted set of numbers. Expected output: an exception."
  [(contains? (sorted-set 1 2 3) :hello)])

(defn num-comparator [i1 i2]
  "Java-style comparator for numbers. Returns a positive, negative, or zero 
   value based on comparison of i1 and i2."
  (- i1 i2))

(defn num-comparator-test []
  "Tests num-comparator with sample inputs and creates a sorted set using it.
   Expected output: results of comparisons and a sorted set."
  [(num-comparator 1 1)
   (num-comparator 1 10)
   (num-comparator 10 1)
   (sorted-set-by num-comparator 2 1 5 3 4)])

(defn num-comparator-bool [i1 i2]
  "Boolean-style comparator that returns true if i1 is less than i2."
  (< i1 i2))

(defn num-comparator-bool-test []
  "Tests boolean comparator with sorted set creation. 
   Expected output: a sorted set using the boolean comparator."
  [(sorted-set-by num-comparator-bool 2 1 5 3 4)])

(defn default-comparator-test []
  "Uses Clojure's default comparator to compare values of various types. 
   Expected output: results of comparisons."
  [(compare :a :b)
   (compare 1 2)])

(defn total-compare [x y]
  "Comparator that converts values to strings before comparing."
  (compare (str x) (str y)))

(defn custom-comparator-test []
  "Demonstrates sorting with custom comparator `total-compare`, 
   which orders elements as strings. Expected output: sorted set of mixed types."
  [(sorted-set-by total-compare :hello 2 "x" 1 10 false)])
