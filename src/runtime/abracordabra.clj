(ns runtime.abracordabra
  "The book's puzzle on :or defaults that refer to other bindings.

  The answers below are for Clojure 1.13, and they are not the book's.
  Through 1.12 a default could see the bindings made earlier in its own map
  form, so the result depended on the order of the keys. 1.13 evaluates every
  default before the map is taken apart (CLJ-2966), so a default sees only
  what was bound outside the form, and the order of the keys stops mattering.")

;; Map to flip head/tail state
(def flip {:head :tail, :tail :head})

(defn left-then-right
  "Book answer through 1.12: :tail. `(flip left)` saw the default :head.
  On 1.13: nil. `(flip left)` sees the outer `left`, which is nil."
  []
  (let [left nil, right nil
        {:keys [left right] :or {left :head, right (flip left)}} {}]
    [left right]))
;;=> [:head nil]

(defn right-then-left
  "Same form with the keys the other way round.
  Through 1.12 this differed from `left-then-right`; on 1.13 it does not."
  []
  (let [left nil, right nil
        {:keys [right left] :or {left :head, right (flip left)}} {}]
    [left right]))
;;=> [:head nil]

(defn order-matters?
  "The book's question. Through 1.12: true. On 1.13: false."
  []
  (not= (left-then-right) (right-then-left)))
;;=> false

(defn safe-approach
  "Defaults that depend on each other, written as steps. Same on every version."
  []
  (let [{:keys [left right] :or {left :head}} {}
        right (or right (flip left))]
    right))
;;=> :tail

(defn basic-defaults
  []
  (let [{:keys [a b] :or {a 1 b 2}} {}]
    [a b]))
;;=> [1 2]

(def sibling-default-form
  "Through 1.12 this evaluated to [0 1]. On 1.13 it does not compile: there is
  no `x` outside the form for the default of `y` to see. Kept as data so the
  namespace loads; the test compiles it."
  '(let [{:keys [x y] :or {x 0 y (inc x)}} {}]
     [x y]))

(defn safe-computed-defaults
  []
  (let [{:keys [x y] :or {x 0}} {}
        y (or y (inc x))]
    [x y]))
;;=> [0 1]
