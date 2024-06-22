(ns composite-example.variable-light 
  (:require
   [composite-example.switchable :as s]))

(defn make-variable-light [] {:type :variable-light})

(defn set-light-intensity [_intensity])

(defmethod s/turn-on :variable-light [_switchable]
  (set-light-intensity 100))

(defmethod s/turn-off :variable-light [_switchable]
  (set-light-intensity 0))

