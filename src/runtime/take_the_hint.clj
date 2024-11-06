(ns runtime.take-the-hint)

;; Bad type hint - evaluates to function rather than type
(def ^double PI 3.14159)
(= 'double (-> #'PI meta :tag))
;;=> false

;; Show actual metadata tag value
(-> #'PI meta :tag)
;;=> #object[clojure.core$double 0x6b7906b3 "clojure.core$double@6b7906b3"]

;; Correct var type hint with quoted symbol
(def ^{:tag 'double} PI 3.14159)
(-> #'PI meta :tag)
;;=> double

;; Function showing proper type hints
(defn double-doubler 
 "Doubles a double value, demonstrating proper type hints"
 ^double [^double x]
 (* 2 x))

;; Example usage with reflection warnings enabled
(set! *warn-on-reflection* true)

(double-doubler 10)
;;=> 20.0

;; Demonstrate type hints affect Java interop
(Math/abs PI)  ;; no reflection warning with proper hint
;;=> 3.14159

;; Example showing constant value optimization
(def ^{:tag 'double :const true} PI-CONST 3.14159)

;; Compare bytecode (shown in comments)
(Math/abs PI)
;; Look up var, unbox Double to double, call abs
;; getstatic ...
;; invokevirtual ...
;; checkcast ...
;; invokestatic ...

(Math/abs PI-CONST)
;; Direct use of constant double value
;; ldc2_w 3.14159
;; invokestatic java/lang/Math.abs:(D)D

;; Additional examples showing type hints

;; Array type hints
(defn sum-array ^double [^doubles arr]
 (areduce arr i ret 0.0
          (+ ret (aget arr i))))

;; Class type hints
(defn string-length ^long [^String s]
 (.length s))

;; Show reflection warnings
(defn no-hints [x]
 (.length x))
;; Reflection warning, call to length can't be resolved.
