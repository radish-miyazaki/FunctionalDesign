(ns wa-tor.water
  (:require
   [wa-tor.cell :as cell]
   [wa-tor.water :as water]))

(defn make [] {::cell/type ::water})

(defn is? [cell]
  (= ::water (::cell/type cell)))

