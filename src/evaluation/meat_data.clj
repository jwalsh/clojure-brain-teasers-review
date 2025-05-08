(ns evaluation.meat-data
  "Demonstrates Clojure metadata usage and behaviors"
  (:require [clojure.test :as t]))

;; Function demonstrating basic metadata assignment
(defn the-road-so-far 
  "Shows different ways to attach metadata to variables"
  []
  (let [the-winchesters "Sam and Dean Winchester"
        four-horsemen (with-meta ["war", "famine", "Pestilence", "Death"] {:meta "vector"})
        apocalypse-now (java.util.Date.)]
    {:date apocalypse-now
     :hunters the-winchesters
     :threat four-horsemen}))

;; Collection with a string inside
(def song ["Carry On My Wayward Son"])

;; Function showing different metadata scenarios
(defn visions
  "Demonstrates various metadata usage patterns"
  []
  (let [weapon      'the-colt  ;; Can't add metadata to keywords
        num-of-episodes 666    ;; regular number
        chevy-impala (with-meta 'baby {:year "1967"})   ;; metadata on a symbol
        demon       "Ruby "    ;; regular string
        ]
    {:weapon  weapon
     :car     chevy-impala
     :demon   demon
     :episode num-of-episodes}))

;; Function to examine metadata (note: corrected syntax from original)
(defn metatrons-divine-notes
  "Returns metadata from the values"
  [road-data vision-data]
  {:road-meta (meta (:threat road-data))
   :vision-meta {:weapon (meta (:weapon vision-data))
                 :car (meta (:car vision-data))
                 :demon (meta (:demon vision-data))
                 :episode (meta (:episode vision-data))}})

;; Additional examples showing metadata behavior

(defn metadata-on-symbols
  "Demonstrates metadata on symbols"
  []
  (let [s (with-meta 'mysymbol {:doc "This is a symbol" :deprecated true})]
    [(meta s)
     ;; Don't eval - will try to resolve 'mysymbol
     nil]))

(defn metadata-on-collections
  "Shows metadata behavior with collections"
  []
  (let [v (with-meta [1 2 3] {:type "vector" :immutable true})
        m (with-meta {:a 1 :b 2} {:type "map"})
        s (with-meta #{1 2 3} {:type "set"})]
    {:vector-meta (meta v)
     :map-meta (meta m)
     :set-meta (meta s)
     ;; Metadata doesn't survive most operations
     :conj-meta (meta (conj v 4))
     ;; But specific operations preserve it
     :with-meta-preserved (meta (with-meta (conj v 4) (meta v)))}))

(defn type-hints-usage
  "Shows type hints as metadata"
  []
  {:string-meta (meta (with-meta 'field {:tag String}))
   :long-meta (meta (with-meta 'num {:tag Long}))
   :private-meta (meta (with-meta 'x {:private true}))})

(comment
  ;; REPL experiments
  
  ;; Basic metadata access
  (meta ^{:foo "bar"} [1 2 3])          ;; => {:foo "bar"}
  (meta (with-meta [1 2 3] {:hi "there"})) ;; => {:hi "there"}
  
  ;; Type hints are metadata
  (meta #'the-road-so-far)               ;; Shows function metadata
  
  ;; Metadata doesn't survive most operations
  (def v (with-meta [1 2 3] {:important true}))
  (meta v)                               ;; => {:important true}
  (meta (conj v 4))                      ;; => nil
  
  ;; Exploring metadata on vars
  (def ^:private ^:dynamic *config*)
  (meta #'*config*)                      ;; => {:private true, :dynamic true}
  
  ;; Reader macros and metadata
  (meta song)                            ;; Reader macro comment skips metadata
  
  ;; Run tests inline
  (t/run-tests))