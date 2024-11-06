(ns runtime.boom-goes-the-dynamite)


(defn doubled [n]
  (loop [i 0
         output ()]
    (if (< i n)
      (recur (inc i)
             (concat output [i i]))
      output)))

(doubled 3)
(= 0 (first (doubled 50000)))


(defn concat1
  ([] (lazy-seq nil))
  ([coll] (lazy-seq coll))
  ([coll1 coll2]
   (lazy-seq
    (let [s (seq coll1)]
      (if s
        (cons (first s) (concat1 (rest s) coll2))
        coll2)))))

(concat1 [])

(concat1 [1 2 3])

(concat1 [1 2 3] [4 5 6])

(defn doubled-take2 [n]
  (loop [i 0
         output []]
    (if (< i n)
      (recur (inc i) (conj output i i))
      output)))

(first (doubled-take2 50000))
