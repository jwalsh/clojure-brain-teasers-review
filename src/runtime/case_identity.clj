(ns runtime.case-identity)

(def photo1 :hero)
(def photo2 :villian)

(defn identify [photo]
  (case photo
    photo1 "Our Hero!"
    photo2 "The dastardly villian"
    "Unknown"))

(identify photo1)
(identify 'photo1)
