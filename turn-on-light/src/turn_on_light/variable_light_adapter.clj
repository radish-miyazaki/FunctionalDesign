(ns turn-on-light.variable-light-adapter
  (:require
   [turn-on-light.switchable :as s]
   [turn-on-light.variable-light :as v-l]))

(defn make-adapter []
  {:type :variable-light})

(defmethod s/turn-on :variable-light [_switchable]
  (v-l/turn-on-light 100))

(defmethod s/turn-off :variable-light [_switchable]
  (v-l/turn-on-light 0))

