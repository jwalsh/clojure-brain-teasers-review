(ns collections.off-record-test
  (:require [clojure.test :refer [deftest testing is]]
            [collections.off-record :as sut]))

(deftest album-record-test
  (testing "Album record behavior"
    (testing "instance? check after dissoc"
      (is (false? (first (sut/queen-test)))
          "Removing required field turns record into regular map"))

    (testing "setting record field to nil"
      (let [album-with-nil (second (sut/queen-test))]
        (is (instance? collections.off_record.Album album-with-nil)
            "Setting field to nil retains record type")
        (is (= "News of the World" (:name album-with-nil))
            "Name field remains unchanged")
        (is (nil? (:artist album-with-nil))
            "Artist field is set to nil")))

    (testing "properties of original record"
      (is (instance? collections.off_record.Album sut/news-of-the-world)
          "Original instance is an Album record")
      (is (= "Queen" (:artist sut/news-of-the-world))
          "Original artist is Queen")
      (is (= "News of the World" (:name sut/news-of-the-world))
          "Original name is News of the World")))

  (testing "Edge cases"
    (testing "dissoc behavior" 
      (let [map-result (dissoc sut/news-of-the-world :artist)]
        (is (map? map-result)
            "Result is a regular map")
        (is (= {:name "News of the World"} map-result)
            "Only name field remains")))

    (testing "adding extra fields"
      (let [extended (assoc sut/news-of-the-world :year 1977)]
        (is (instance? collections.off_record.Album extended)
            "Adding fields preserves record type")
        (is (= 1977 (:year extended))
            "Can add extra fields to record")))))
