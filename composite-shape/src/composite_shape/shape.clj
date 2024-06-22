(ns composite-shape.shape
  (:require
   [clojure.spec.alpha :as s]))

(s/def ::type keyword?)
(s/def ::shape-type (s/keys :req [::type]))

(defmulti translate (fn [shape _dx _dy] (::type shape)))

(defmulti scale (fn [shape _factor] (::type shape)))

