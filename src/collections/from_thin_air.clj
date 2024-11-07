(ns collections.from-thin-air
  "Demonstrates how nil is handled polymorphically by Clojure collection functions,
   creating new collections or acting as empty collections depending on context"
  (:require
   [clojure.string :as str]))

(defn test-assoc-nil
  "Tests that assoc on nil creates a new map rather than returning nil.
   Demonstrates polymorphic creation of collections 'from thin air'"
  []
  [(nil? (assoc nil :ex :nihilo))    ;; false - creates map
   (= {:ex :nihilo}
      (assoc nil :ex :nihilo))])     ;; new map springs into existence

(defn test-nil-as-empty
  "Tests how nil acts as an empty collection for various operations.
   Shows consistent behavior across collection functions for retrieval
   and inspection operations"
  []
  [(count nil)                       ;; 0 - empty collection
   (nth nil 0)                       ;; nil - no element
   (nth nil (count nil))             ;; nil - no element
   (get nil :some-key)               ;; nil - no key found
   (contains? nil :some-key)         ;; false - no keys
   (first nil)                       ;; nil - no first element
   (rest nil)])                      ;; () - empty sequence

(defn test-conj-nil
  "Tests that conj on nil creates a new vector rather than returning nil.
   Another example of collections springing into existence when needed"
  []
  [(nil? (conj nil 1))               ;; false - creates vector
   (= [1] (conj nil 1))])            ;; new vector springs into existence

(defn test-java-interop
  "Demonstrates that nil handling does not extend to Java interop.
   Will throw NullPointerException, showing where polymorphic nil
   handling stops"
  []
  [(str/capitalize nil)])            ;; throws NPE
