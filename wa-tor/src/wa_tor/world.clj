(ns wa-tor.world 
  (:require
   [clojure.spec.alpha :as s]
   [wa-tor.cell :as cell]))

;; Specifications
(s/def ::location (s/tuple int? int?))
(s/def ::cell #(contains? % ::cell/type))
(s/def ::cells (s/map-of ::location ::cell))
(s/def ::bounds ::location)
(s/def ::world (s/and (s/keys :req [::cells ::bounds])
                      #(= (::type %) ::world)))

;; Multimethods
(defmulti tick ::type)
(defmulti make-cell (fn [factory-type _cell-type] factory-type))

;; Functions
(defn- wrap [world [x y]]
  (let [[w h] (::bounds world)]
    [(mod x w) (mod y h)]))
(defn make [w h]
  {:post [(s/valid? ::world %)]}
  (let [locs (for [x (range w) y (range h)] [x y])
        default-cell (make-cell ::world :default-cell)
        loc-water (interleave locs (repeat default-cell))
        cells (apply hash-map loc-water)]
    {::type ::world
     ::cells cells
     ::bounds [w h]}))
(defn set-cell [world loc cell]
  (assoc-in world [::cells loc] cell))
(defn get-cell [world loc]
  (get-in world [::cells loc]))
(defn neighbors [world loc]
  (let [[x y] loc
        neighbors (for [dx (range -1 2) dy (range -1 2)]
                    (wrap world [(+ x dx) (+ y dy)]))]
    (remove #(= loc %) neighbors))) ; 現在のセルは取り除く

