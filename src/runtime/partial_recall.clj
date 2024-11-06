(ns runtime.partial-recall)

(defn sentence 
  "Creates a question asking if the subject is an object.
   Returns a string in the format 'Is subject a object?'"
  [subject object]
  (str "Is " subject " a " object "?"))

;; Initial value examples
(def planet "Mars")

;; Two approaches to creating functions with fixed subject
(def f1 (partial sentence planet))
(def f2 (fn [object] (sentence planet object)))

;; Test initial behavior
(f1 "dream")
;;=> "Is Mars a dream?"
(f2 "dream")
;;=> "Is Mars a dream?"

;; Redefine the planet var
(def planet "Earth")

;; Demonstrate different behavior after redefinition
(f1 "dream")
;;=> "Is Mars a dream?"  ;; f1 captured original value
(f2 "dream")
;;=> "Is Earth a dream?" ;; f2 uses current var value

;; Show they are no longer equal
(= (f1 "dream") (f2 "dream"))
;;=> false

;; Additional examples showing var behavior

;; Creating function that explicitly deref var
(def f3 (fn [object] (sentence @(var planet) object)))

;; Creating function with delay
(def f4 
  (let [p (delay planet)]
    (fn [object] (sentence @p object))))

;; Compare all approaches
(def tests ["planet" "dream" "mirage"])

(println "\nResults for different function approaches:")
(doseq [test tests]
  (println "Test:" test)
  (println "f1 (partial):" (f1 test))
  (println "f2 (fn):" (f2 test))
  (println "f3 (var):" (f3 test))
  (println "f4 (delay):" (f4 test))
  (println))
