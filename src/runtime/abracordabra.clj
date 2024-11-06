(ns runtime.abracordabra)

;; Map to flip head/tail state
(def flip {:head :tail, :tail :head})

;; Example showing order sensitivity in destructuring with :or
(let [empty {}
      left nil, right nil
      {:keys [left right] :or {left :head, right (flip left)}} empty
      right-1 right
      
      ;; Reset bindings and try other order
      left nil, right nil  
      {:keys [right left] :or {left :head, right (flip left)}} empty
      right-2 right]
  
  (println "Results:" right-1 right-2)
  (= right-1 right-2))
;;=> Results: :tail nil
;;=> false

;; Example showing safer approach with explicit steps
(let [empty {}
      left nil
      right nil
      ;; Destructure with simple defaults
      {:keys [left right] :or {left :head}} empty  
      ;; Explicitly handle right value
      right (or right (flip left))]
  right)
;;=> :tail

;; Additional examples showing destructuring behavior 

;; Basic :or usage
(let [{:keys [a b] :or {a 1 b 2}} {}]
 [a b])
;;=> [1 2]

;; Order dependence with computed defaults
(let [{:keys [x y] :or {x 0 y (inc x)}} {}]
 [x y])
;;=> [0 1]

(comment
  (let [{:keys [y x] :or {x 0 y (inc x)}} {}]
    [x y])
)
;;=> Unable to resolve symbol

;; Safe computed defaults
(let [{:keys [x y] :or {x 0}} {}
     y (or y (inc x))]
 [x y])
;;=> [0 1]
