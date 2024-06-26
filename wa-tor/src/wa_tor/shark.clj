(ns wa-tor.shark
  (:require
   [clojure.spec.alpha :as s]
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]
   [wa-tor.config :as config]))

(s/def ::shark (s/and #(= ::shark (::cell/type %))
                      ::animal/animal))

(defn make []
  {:post [(s/valid? ::shark %)]}
  (merge {::cell/type ::shark}
         (animal/make)))

(defn is? [cell]
  (= ::shark (::cell/type cell)))

(defmethod animal/make-child ::shark [_shark]
  (make))

(defmethod animal/get-reproduction-age ::shark [_shark]
  config/shark-reproduction-age)

(defmethod cell/tick ::shark [shark loc world]
  (animal/tick shark loc world))

(defmethod animal/move ::shark [shark loc world]
  (animal/do-move shark loc world))

(defmethod animal/reproduce ::shark [shark loc world]
  (animal/do-reproduce shark loc world))

(defn eat [shark])

