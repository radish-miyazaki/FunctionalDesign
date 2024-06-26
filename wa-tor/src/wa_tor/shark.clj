(ns wa-tor.shark
  (:require
   [clojure.spec.alpha :as s]
   [wa-tor.animal :as animal]
   [wa-tor.cell :as cell]
   [wa-tor.config :as config]
   [wa-tor.fish :as fish]
   [wa-tor.water :as water]
   [wa-tor.world :as world]))

(s/def ::health int?)
(s/def ::shark (s/and #(= ::shark (::cell/type %))
                      ::animal/animal
                      (s/keys :req [::health])))

(defn make []
  {:post [(s/valid? ::shark %)]}
  (merge {::cell/type ::shark
          ::health config/shark-starting-health}
         (animal/make)))

(defn health [shark] (::health shark))

(defn set-health [shark new-health] (assoc shark ::health new-health))

(defn decrement-health [shark] (update shark ::health dec))

(defn is? [cell] (= ::shark (::cell/type cell)))

(defn- feed [shark]
  (let [new-health (min config/shark-max-health
                        (+ (health shark) config/shark-eating-health))]
    (assoc shark ::health new-health)))

(defn eat [shark loc world]
  (let [neighbors (world/neighbors world loc)
        fishy-neighbors (filter #(fish/is? (world/get-cell world %)) neighbors)]
    (if (empty? fishy-neighbors)
      nil
      [{loc (water/make)}
        {(rand-nth fishy-neighbors) (feed shark)}])))

(defmethod animal/make-child ::shark [_shark] (make))

(defmethod animal/get-reproduction-age ::shark [_shark] config/shark-reproduction-age)

(defmethod cell/tick ::shark [shark loc world]
  (if (= 1 (health shark))
    [nil {loc (water/make)}]
    (let [aged-shark (-> shark
                         (animal/increment-age)
                         (decrement-health))]
      (if-let [reproduction (animal/reproduce aged-shark loc world)]
        reproduction
        (if-let [eaten (eat aged-shark loc world)]
          eaten
          (animal/move aged-shark loc world)
          )))))

(defmethod animal/move ::shark [shark loc world] (animal/do-move shark loc world))

(defmethod animal/reproduce ::shark [shark loc world]
  (if (>= (health shark) config/shark-reproduction-health)
    (animal/do-reproduce shark loc world)
    nil))

