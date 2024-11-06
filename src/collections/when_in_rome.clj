(ns collections.when-in-rome
  "Demonstrates how conj behaves differently for lists and vectors")

(defn test-list-conj
  "Test conj behavior with lists (adds at beginning)"
  []
  [(= (conj '(:colosseum :vatican) :pantheon :trevi-fountain)
      '(:trevi-fountain :pantheon :colosseum :vatican))])

(defn test-vector-conj
  "Test conj behavior with vectors (adds at end)"
  []
  [(= (conj [:colosseum :vatican] :pantheon :trevi-fountain)
      [:colosseum :vatican :pantheon :trevi-fountain])])


(defn test-retaining-order
  "Test how map vs mapv return different collection types"
  []
  [(map #(+ % 3) (range 5))    ;; returns a lazy seq
   (mapv #(+ % 3) (range 5))
   (into [] (map #(+ % 3) (range 5)))]) ;; returns a vector


(defn test-set-conj
  "Test conj behavior with sets (adds unique elements)"
  []
  [(conj #{:colosseum :vatican} :pantheon :trevi-fountain)  ;; unordered, unique
   (conj #{1 2 3} 4 4 4)])  ;; duplicates ignored
