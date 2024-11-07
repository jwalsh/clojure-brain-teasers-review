(ns basics.vacuums-suck
  "Demonstrates the concept of vacuous truth with empty collections")

(defn test-empty-and
  "Test and with no arguments"
  []
  [(and)]) ;; Returns true due to vacuous truth

(defn test-empty-every
  "Test every? with empty collection"
  []
  [(every? odd? [])]) ;; Returns true due to vacuous truth

(defn test-empty-or
  "Test or with no arguments"
  []
  [(or)]) ;; Returns nil, as or needs at least one truthy value
