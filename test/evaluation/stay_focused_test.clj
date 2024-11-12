(ns evaluation.stay-focused-test
  (:require [clojure.test :refer :all]
            [evaluation.stay-focused :refer :all]))

(deftest test-my-msgs-structure
  (testing "Message map structure"
    (is (map? my-msgs) "my-msgs should be a map")
    (is (contains? my-msgs :emails) "should contain :emails key")
    (is (contains? my-msgs :discord-msgs) "should contain :discord-msgs key")
    (is (contains? my-msgs :voicemails) "should contain :voicemails key")))

(deftest test-unread-count
  (testing "Unread message counting"
    (let [sample-msgs {:emails [[:from "test1"] [:from "test2"]]
                      :discord-msgs {"Room1" 5 "Room2" 3}
                      :voicemails ["msg1" "msg2"]}]
      (is (= 12 (unread sample-msgs)) "Should correctly sum all unread messages"))))

(deftest test-discard-reader
  (testing "Reader macro discarding"
    (let [results (discard-reader-test)]
      (is (= [true] results) "Should match expected count with discarded items"))))