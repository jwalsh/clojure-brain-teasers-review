(ns evaluation.roll-dice)

(defn duplicate-set-key-test
  "Tests the behavior of sets with duplicate keys."
  []
  [(= #{(rand-int 6) (rand-int 6)}  ; Sets automatically deduplicate keys.
      #{(rand-int 6) (rand-int 6)})
   (conj #{1 2} 2)                  ; conj with duplicate key has no effect.
   #{1 2 1}                         ; Sets can only contain unique elements.
   (conj #{} (rand-int 6) (rand-int 6))]) ; Deduplication on empty set.
