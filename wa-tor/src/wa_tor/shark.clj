(ns wa-tor.shark
  (:require
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]))

(defn make []
  (merge {::cell/type ::shark}
         (animal/make)))

(defmethod cell/tick ::shark [shark]
  (animal/tick shark))

(defmethod animal/move ::shark [shark]
  )

(defmethod animal/reproduce ::shark [shark]
  )

(defn eat [shark]
  )

