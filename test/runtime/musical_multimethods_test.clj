(ns runtime.musical-multimethods-test
  (:require [clojure.test :refer :all]
            [runtime.musical-multimethods :refer :all]))

(deftest subject-test
  (testing "Musical subject is defined correctly"
    (is (vector? subject))
    (is (= ["A" "D" "E" "A"] subject))))

(deftest default-method-test
  (testing "Default method returns original subject"
    (is (= subject (play "original" :default)))))

(deftest retrograde-test
  (testing "Retrograde transformation reverses the subject"
    (is (= ["A" "E" "D" "A"] (play "backwards" :retrograde)))))

(deftest transpose-minor-test
  (testing "Transpose to minor adds 'm' suffix to each note"
    (is (= ["Am" "Dm" "Em" "Am"] (play "to-minor-key" :transpose-minor)))))

(deftest mobius-test
  (testing "Mobius transformation creates crab canon pattern"
    (is (= ["A" "D" "A" "E" "A" "D"] (play "crab-canon" :mobius)))))

(deftest transpose-test
  (testing "Transpose shifts notes by interval"
    (is (= ["E" "A" "B" "E"] (play "up-a-fifth" :transpose 4)))
    (is (= ["D" "G" "A" "D"] (play "up-a-fourth" :transpose 3)))))

(deftest augmentation-test
  (testing "Augmentation repeats each note"
    (is (= ["A" "A" "D" "D" "E" "E" "A" "A"] 
           (play "doubled" :augmentation 2)))
    (is (= ["A" "A" "A" "D" "D" "D" "E" "E" "E" "A" "A" "A"] 
           (play "tripled" :augmentation 3)))))

(deftest inversion-test
  (testing "Inversion flips the intervals"
    (is (= ["G" "D" "C" "G"] (play "inverted" :inversion)))))

(deftest echo-test
  (testing "Echo adds volume indicators"
    (is (= ["Af" "Amp" "Df" "Dmp" "Ef" "Emp" "Af" "Amp"] 
           (play "with-dynamics" :echo ["f" "mp"])))))

(deftest hierarchy-test
  (testing "Hierarchy-based dispatch"
    ;; Base music type
    (is (= ["Do" "Re" "Mi" "Fa"] (play "music" :runtime.musical-multimethods/music)))
    
    ;; Jazz inherits from music but has its own implementation
    (is (= ["Bb7" "Eb7" "Ab7" "Db7"] (play "jazz" :runtime.musical-multimethods/jazz)))
    
    ;; Bebop is a kind of jazz
    (is (= ["Cmaj7" "A7" "Dm7" "G7"] (play "bebop" :runtime.musical-multimethods/bebop)))
    
    ;; Baroque inherits from classical which inherits from music
    (is (= ["Do" "Re" "Mi" "Fa"] (play "baroque" :runtime.musical-multimethods/baroque)))))

(deftest dispatch-mechanism-test
  (testing "Multimethod dispatches on the second argument"
    (let [result1 (play "doesn't matter" :retrograde)
          result2 (play "something else" :retrograde)]
      ;; Same transformation should give same result regardless of first arg
      (is (= result1 result2))
      (is (= ["A" "E" "D" "A"] result1)))))

(deftest multimethod-metadata-test
  (testing "Multimethod has appropriate metadata"
    ;; MultiFn is callable but not a fn?
    (is (instance? clojure.lang.IFn play))
    (is (= clojure.lang.MultiFn (type play)))
    (is (string? (:doc (meta #'play))))
    (is (.contains ^String (:doc (meta #'play)) "musical pattern"))))