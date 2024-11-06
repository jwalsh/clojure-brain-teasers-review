(ns evaluation.stay-focused)

(def my-msgs
  "A map representing different types of notifications with selectively 
   discarded entries using the `#_` reader macro."
  {:emails [[:from "boss"] [:from "mom"] #_ [:from "Nigerian Prince"] #_ [:from "LinkedIn"]]
   :discord-msgs {"Clojure Camp" 6 #_ "Heart of Clojure" #_ 3 "DungeonMasters" 20}
   :voicemails ["Your voicemail box is full."]})

(defn unread
  "Calculates the total count of unread notifications by summing emails,
   discord messages, and voicemails. Discarded items are skipped.
   Returns the total count as an integer."
  [msgs]
  (let [{:keys [emails discord-msgs voicemails]} msgs]
    (+ (count emails)
       (reduce + (vals discord-msgs))
       (count voicemails))))

(defn discard-reader-test []
  "Tests the unread count with `my-msgs` to verify selective discarding.
   Returns true if the count is as expected, otherwise false."
  [(= 29 (unread my-msgs))])
