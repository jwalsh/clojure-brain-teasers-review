(ns evaluation.parental-advisory-test
  (:require [clojure.test :refer [deftest testing is]]
            [evaluation.parental-advisory :as sut]))

(deftest color-pairing-test
  (testing "Basic color pairing functionality"
    (let [result (sut/pair-colors)]
      (testing "structure"
        (is (= 3 (count result))
            "Returns expected number of pairs")
        (is (every? vector? result)
            "Each pair is a vector")
        (is (every? #(= 2 (count %)) result)
            "Each pair has two elements"))
      
      (testing "content"
        (is (= [["Scarlet" "FD0E35"]
                ["Dandelion" "FED85D"]
                ["Cerulean" "02A4D3"]]
               result)
            "Pairs colors with correct hex codes"))))

  (testing "Error cases"
    (testing "vector invocation error"
      (is (thrown? clojure.lang.ArityException 
                   (doall (sut/pair-colors-error)))
          "Attempting to call vector with no args throws ArityException"))))

(deftest pair-colors-verification-test
  (testing "Pair colors verification function"
    (is (true? (sut/pair-colors-test))
        "Verification confirms correct pairing")))

(deftest vector-invocation-test
  (testing "Vector as function demonstration"
    (is (= :hi ([:hi :there] 0))
        "Vectors can be used as functions to access elements by index")))
