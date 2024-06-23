(ns visitor-example.shape)

(defmulti translate (fn [shape _dx _dy] (:type shape)))
(defmulti scale (fn [shape _factor] (:type shape)))

