(ns destructuring.return-to-sender
  "Take part of a map out, change it, put it back. (Clojure 1.13)

  :select is the part of the map the binding form asked for, as a map.
  :excess is everything it did not ask for. `merge-deep` puts two such maps
  back together. Read as a lens: :select is get, and merging a view over
  the excess is put.")

(def order
  {:id 7
   :note nil
   :ship {:city "Boston" :gate 4}})

(defn take-apart
  "The form asks for :id, and for :zip under :ship. There is no :zip.
  What is the view, and what is the excess?

  The view keeps :ship as an empty map: the form looked inside it and found
  nothing it wanted. The excess keeps :ship too, with everything in it."
  []
  (let [{:keys [id] {:keys [zip]} :ship :select view :excess leftover} order]
    {:view view :excess leftover}))
;;=> {:view {:id 7, :ship {}}, :excess {:note nil, :ship {:city "Boston", :gate 4}}}

(defn put-back-unchanged
  "Put the view back without touching it. Do you get the order back?
  Yes. Nothing was lost in the split. (The lens law GetPut.)"
  []
  (let [{:keys [id] {:keys [zip]} :ship :select view :excess leftover} order]
    (= order (merge-deep view leftover))))
;;=> true

(defn delete-then-look-again
  "Now remove :ship from the view, put the view back, and take the view
  again with the same form. Is :ship still gone?

  No. The excess still holds the city and the gate under :ship, so putting
  back restores the key, and the second look finds :ship as an empty map.
  You put {:id 7} and got {:id 7, :ship {}} back. (The lens law PutGet,
  failing.) Hidden data survived, and the price is that the delete did not."
  []
  (let [{:keys [id] {:keys [zip]} :ship :select view :excess leftover} order
        edited (dissoc view :ship)
        put-back (merge-deep edited leftover)
        {:keys [id] {:keys [zip]} :ship :select second-look} put-back]
    {:put edited :got second-look :same? (= edited second-look)}))
;;=> {:put {:id 7}, :got {:id 7, :ship {}}, :same? false}
