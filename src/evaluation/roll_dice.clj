(ns evaluation.roll-dice)

(defn duplicate-set-key-test
  "Tests the behavior of sets with duplicate keys."
  []
  [;; Use conj to test runtime deduplication (instead of literal syntax)
   (let [r1 (rand-int 6)
         r2 (rand-int 6)]
     (= (conj #{} r1 r2) 
        (conj #{} r1 r2)))
   
   ;; conj with duplicate key has no effect
   (conj #{1 2} 2)
   
   ;; Use conj for testing set uniqueness (can't use literal #{1 2 1})
   (conj (conj #{1} 2) 1)
   
   ;; Deduplication on empty set with random values
   (conj #{} (rand-int 6) (rand-int 6))])
