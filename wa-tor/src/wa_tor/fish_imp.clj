(ns wa-tor.fish-imp
  (:require
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]
   [wa-tor.config :as config]
   [wa-tor.fish :as fish]
   [wa-tor.shark :as shark]))

(defmethod cell/tick ::fish/fish [fish loc world]
  (if (> (rand) config/fish-evalution-rate)
    [nil {loc (shark/make)}]
    (animal/tick fish loc world)))

(defmethod animal/move ::fish/fish [fish loc world]
  (animal/do-move fish loc world))

(defmethod animal/reproduce ::fish/fish [fish loc world]
  (animal/do-reproduce fish loc world))

