(ns wa-tor.fish-imp
  (:require
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]
   [wa-tor.fish :as fish]))

(defmethod cell/tick ::fish/fish [fish loc world]
  (animal/tick fish loc world))

(defmethod animal/move ::fish/fish [fish loc world]
  (animal/do-move fish loc world))

(defmethod animal/reproduce ::fish/fish [fish loc world]
  (animal/do-reproduce fish loc world))

