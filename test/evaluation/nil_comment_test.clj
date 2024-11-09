(ns evaluation.nil-comment-test
  (:require [clojure.test :refer [deftest testing is]]
            [evaluation.nil-comment :as sut]))

(deftest nil-comment-evaluation-test
  (testing "Comment form evaluation to nil in collection"
    (let [[result] (sut/nil-coment-test)]
      (testing "attempt to increment nil from commented form"
        (is (thrown? NullPointerException (doall result))
            "Incrementing nil from (comment) throws NullPointerException")))))

(deftest comment-form-test
  (testing "Comment form behavior"
    (is (nil? (comment 100))
        "Comment form evaluates to nil")
    
    (is (= 1 (count (filter nil? [1 2 (comment 3) 4])))
        "Comment form produces nil in collections")
    
    (testing "comment with multiple forms"
      (is (nil? (comment 
                  (+ 1 2)
                  (* 3 4)
                  "ignored"))
          "Multiple form comment still evaluates to nil"))))
