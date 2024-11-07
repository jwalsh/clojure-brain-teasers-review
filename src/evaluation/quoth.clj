(ns evaluation.quoth)

(def raven "nevermore")

(defn syntax-quote-tests
  []
  [(= raven "nevermore")
   (= 'raven (symbol "raven"))
   (not= 'raven (symbol "user/raven"))
   (= '(a b c) '(a b c))])
