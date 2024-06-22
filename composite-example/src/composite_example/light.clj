(ns composite-example.light
  (:require
   [composite-example.switchable :as s]))

(defn make-light [] {:type :light})

(defn turn-on-light [])

(defn turn-off-light [])

(defmethod s/turn-on :light [_switchable]
  (turn-on-light))

(defmethod s/turn-off :light [_switchable]
  (turn-off-light))

