(ns wa-tor.water-imp
  (:require
   [wa-tor.cell :as cell]
   [wa-tor.config :as config]
   [wa-tor.fish :as fish]
   [wa-tor.water :as water]))

;; Implementations
(defmethod cell/tick ::water/water [water loc _world]
  (if (> (rand) config/water-evalution-rate)
    [nil {loc (fish/make)}]
    [nil {loc water}]))

