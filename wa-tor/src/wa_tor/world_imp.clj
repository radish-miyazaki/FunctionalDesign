(ns wa-tor.world-imp
  (:require
   [wa-tor.cell :as cell]
   [wa-tor.fish :as fish]
   [wa-tor.shark :as shark]
   [wa-tor.water :as water]
   [wa-tor.world :as world :refer :all]))

(defmethod world/tick ::world/world [world]
  (let [cells (::world/cells world)]
        (loop [locs (keys cells)
               new-cells {}
               moved-into #{}]
          (cond
            (empty? locs)
            (assoc world ::world/cells new-cells)

            (contains? moved-into (first locs))
            (recur (rest locs) new-cells moved-into)

            :else
            (let [loc (first locs)
                  cell (get cells loc)
                  [from to] (cell/tick
                             cell
                             loc
                             (assoc world :moved-into moved-into))
                  new-cells (-> new-cells (merge from) (merge to))
                  to-loc (first (keys to))
                  to-cell (get to to-loc)
                  moved-into (if (water/is? to-cell)
                               moved-into
                               (conj moved-into to-loc))]
              (recur (rest locs) new-cells moved-into))))))

(defmethod world/make-cell ::world/world [_factory-type cell-type]
  (condp = cell-type
    :default-cell (water/make)
    :water (water/make)
    :fish (fish/make)
    :shark (shark/make)))

