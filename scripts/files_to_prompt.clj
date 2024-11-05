#!/usr/bin/env bb

(ns files-to-prompt
  (:require [babashka.fs :as fs]
            [clojure.string :as str]))

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
  (let [files (or args ["src/basics/truthy_or_dare.clj" 
                       "test/basics/truthy_or_dare_test.clj"])]
    (doseq [file files]
      (when (fs/exists? file)
        (println (file->prompt file))))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
