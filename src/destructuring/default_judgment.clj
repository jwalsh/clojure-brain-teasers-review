(ns destructuring.default-judgment
  "Which `a` does a default see? (Clojure 1.13)

  One name is bound twice: outside the map form, and by the form itself.
  The default for `b` mentions it.")

(defn which-a
  "The map supplies :a, so the form binds a to 1. :b is absent, so b takes
  its default, which is `a`. Guess before reading on.

  Through 1.12 the default saw the form's own a: [1 1].
  On 1.13 defaults are evaluated before the map is taken apart, so the
  default sees the outer a: [1 100]."
  []
  (let [a 100
        {:keys [a b] :or {b a}} {:a 1}]
    [a b]))
;;=> [1 100]

(defn default-not-needed
  "When the key is present the default's value is not used, whatever it saw."
  []
  (let [a 100
        {:keys [a b] :or {b a}} {:a 1 :b 2}]
    [a b]))
;;=> [1 2]

(defn present-but-nil
  "A key that is present with a nil value is present. No default."
  []
  (let [{:keys [b] :or {b :default}} {:b nil}]
    b))
;;=> nil
