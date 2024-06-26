(ns wa-tor.fish
  (:require
   [clojure.spec.alpha :as s]
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]
   [wa-tor.config :as config]))

(s/def ::fish (s/and #(= ::fish (::cell/type %))
                     ::animal/animal))

(defn is? [cell]
  (= ::fish (::cell/type cell)))

(defn make []
  {:post [(s/valid? ::fish %)]}
  (merge {::cell/type ::fish}
         (animal/make)))

(defmethod animal/make-child ::fish [_fish]
  (make))

(defmethod animal/get-reproduction-age ::fish [_fish]
  config/fish-reproduction-age)

