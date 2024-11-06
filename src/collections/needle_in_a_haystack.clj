(ns collections.needle-in-a-haystack)

(def haystack
  "A shuffled vector containing 0-99 and :needle, demonstrating linear search."
  (shuffle (conj (range 100) :needle)))

(defn contains-test []
  "Tests index-based key lookup in vectors. Returns results for :needle and 50."
  [(contains? haystack :needle)
   (contains? haystack 50)])

(def haystack-set
  "A set with 0-99 and :needle, illustrating constant-time lookup with sets."
  (set (conj (range 100) :needle)))

(defn indexed-data-structures-test []
  "Tests constant-time lookup in sets. Returns whether :needle is present."
  [(contains? haystack-set :needle)])

(defn linear-search-test []
  "Uses filter and some for linear search. Returns first match of :needle."
  [(first (filter #{:needle} haystack-set))
   (some #{:needle} haystack-set)])

(defn predicates-test []
  "Demonstrates predicates with odd? using filter and some. Returns first match."
  [(first (filter odd? (range 10)))
   (some odd? (range 10))])
