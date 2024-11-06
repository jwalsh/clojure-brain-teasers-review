(ns collections.off-record)

(defrecord Album [name artist]
  "A record representing a musical album with a name and artist.")

(def news-of-the-world
  "Instance of Album for Queen's 'News of the World.'"
  (->Album "News of the World" "Queen"))

(defn queen-test []
  "Illustrates behavior when a required field is removed from a record.
   Returns:
   - A boolean for instance check after `dissoc` (expected to become a map).
   - The record after setting :artist to nil."
  [(instance? Album (dissoc news-of-the-world :artist))
   (assoc news-of-the-world :artist nil)])
