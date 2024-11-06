(ns evaluation.parental-advisory)

(def names ["Scarlet" "Dandelion" "Cerulean"])

(def codes ["FD0E35" "FED85D" "02A4D3"])

(defn pair-colors []
  "Pairs color names with their corresponding hex codes.
   Returns a list of vector pairs like ['Scarlet' 'FD0E35'].
   Demonstrates correct syntax for mapping functions."
  (map vector names codes))

(defn pair-colors-error []
  "Intentionally uses incorrect syntax to demonstrate syntax error
   with inline literal vectors.
   Expected output: a syntax error."
  (map #([%1 %2]) names codes))

(comment
  ([:hi :there] 0)
  (map #(vector %1 %2) names codes))

(defn pair-colors-test []
  "Verifies if `pair-colors` produces the expected pairs.
   Returns true if the pairs match, otherwise false."
  (= (pair-colors) '[["Scarlet" "FD0E35"] ["Dandelion" "FED85D"] ["Cerulean" "02A4D3"]]))
