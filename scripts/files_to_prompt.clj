#!/usr/bin/env bb
(ns files-to-prompt
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

(def source-extensions #{".clj" ".cljc" ".cljs" ".md" ".txt" ".json" ".yaml" ".yml" ".edn"})

(defn read-gitignore []
  (let [gitignore (fs/file ".gitignore")]
    (if (fs/exists? gitignore)
      (->> (str/split-lines (slurp gitignore))
           (remove str/blank?)
           (remove #(str/starts-with? % "#")))
      [])))

(defn glob-match? [name pattern]
  (let [regex (-> pattern
                  (str/replace #"\." "\\.")
                  (str/replace #"\*" ".*"))]
    (boolean (re-matches (re-pattern (str "^" regex "$")) name))))

(defn should-ignore? [path gitignore-rules]
  (let [basename (fs/file-name path)]
    (or (str/starts-with? basename ".")  ; Ignore dot files
        (some #(glob-match? basename %) gitignore-rules))))

(defn source-file? [path]
  (some #(str/ends-with? (str path) %) source-extensions))

(defn get-all-files []
  (let [gitignore-rules (read-gitignore)]
    (->> (fs/glob "." "**")
         (filter fs/regular-file?)
         (remove #(should-ignore? % gitignore-rules))
         (filter source-file?)
         (map str)
         sort)))

(defn file->prompt [file]
  (let [content (slurp file)]
    (format "<documents>
<document index=\"1\">
<source>%s</source>
<document_content>%s</document_content>
</document>
</documents>" 
            (str file)
            content)))

(defn -main [& args]
  (let [[opts files] (loop [[arg & rest-args :as args] args
                           files []
                           xml? false]
                      (cond
                        (empty? args) 
                        [{:xml? xml?} (if (seq files) 
                                      files
                                      (get-all-files))]
                        
                        (or (= "--cxml" arg) (= "-c" arg))
                        (recur rest-args files true)
                        
                        :else 
                        (recur rest-args (conj files arg) xml?)))]
    (doseq [file files]
      (when (fs/exists? file)
        (println (file->prompt file))))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
