(ns runtime.hanging-around
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]))

;; Bad implementation - will throw exception due to closed stream
(defn grep
  "Return lazy sequence of matches to substring
   in the file at path"
  [substring path]
  (with-open [reader (jio/reader path)]
    (->> reader
         line-seq
         (filter #(str/includes? % substring)))))

;; Example usage that will fail
(grep "defn" "src/runtime/hanging_around.clj")
;; Error printing return value (IOException) at
;; java.io.BufferedReader/ensureOpen (BufferedReader.java:122).
;; Stream closed

;; Fixed version - eagerly processes all data before closing stream
(defn grep-eager
  "Return realized sequence of matches to substring in 
   the file at path"
  [substring path]
  (with-open [reader (jio/reader path)]
    (doall
     (->> reader
          line-seq
          (filter #(str/includes? % substring))))))

;; Example usage that works
(grep-eager "defn" "src/runtime/hanging_around.clj")
;;=> ("(defn grep" "(defn grep-eager" "(defn grep-reader")

;; Alternative approach - caller manages reader lifecycle
(defn grep-reader
  "Return lazy sequence of matches to substring 
   in the reader.
   Caller is responsible for closing reader."
  [substring reader]
  (->> reader
       line-seq
       (filter #(str/includes? % substring))))

;; Example usage with caller managing reader
(with-open [rdr (jio/reader "src/runtime/hanging_around.clj")]
  (doall (grep-reader "defn" rdr)))
;;=> ("(defn grep" "(defn grep-eager" "(defn grep-reader")

;; Example showing explicit resource handling
(let [rdr (jio/reader "src/runtime/hanging_around.clj")]
  (try
    (doall (grep-reader "defn" rdr))
    (finally
      (.close rdr))))
;;=> ("(defn grep" "(defn grep-eager" "(defn grep-reader")
