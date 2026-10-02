(ns destructuring.return-to-sender-test
  (:require [clojure.test :refer [deftest is testing]]
            [destructuring.return-to-sender :as sut]))

(deftest taking-apart
  (testing "the view keeps an empty map where it looked and found nothing"
    (is (= {:view {:id 7 :ship {}}
            :excess {:note nil :ship {:city "Boston" :gate 4}}}
           (sut/take-apart)))))

(deftest get-put
  (testing "putting an unchanged view back restores the order"
    (is (true? (sut/put-back-unchanged)))))

(deftest put-get
  (testing "a key deleted from the view comes back, as an empty map"
    (is (= {:put {:id 7} :got {:id 7 :ship {}} :same? false}
           (sut/delete-then-look-again)))))
