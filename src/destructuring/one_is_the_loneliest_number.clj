(ns destructuring.one-is-the-loneliest-number
  "What does a selector return? (Clojure 1.13)

  `selector` turns a binding form into a function. The directives in the
  form (:select, :excess, :missing) say what the function reports.")

(def order
  {:id 7
   :lines [{:sku "A" :bin 9}]})

(defn two-directives
  "Ask for two things and you get a map with two keys."
  []
  ((selector {:keys [id] :select _ :excess _}) order))
;;=> {:select {:id 7}, :excess {:lines [{:sku "A", :bin 9}]}}

(defn one-directive
  "Ask for one thing. Do you get a map with one key?

  No. You get the thing itself, with no wrapper."
  []
  ((selector {:keys [id] :excess _}) order))
;;=> {:lines [{:sku "A", :bin 9}]}

(defn the-bug-this-causes
  "So this reads :excess out of the excess. It finds no such key and says
  nil, which looks exactly like 'nothing extra here'."
  []
  (:excess ((selector {:keys [id] :excess _}) order)))
;;=> nil

(defn excess-stops-at-vectors
  "The form names :id and :lines. Each line has a :bin nobody asked for.
  Is :bin in the excess?

  No. Directives look inside nested maps, not inside vectors. :lines was
  asked for, so all of it counts as taken, and nil means no excess."
  []
  (let [{:keys [id lines] :excess leftover} order]
    leftover))
;;=> nil

(defn one-form-per-line
  "To see inside the vector, map a selector over it."
  []
  (mapv (selector {:keys [sku] :excess _}) (:lines order)))
;;=> [{:bin 9}]
