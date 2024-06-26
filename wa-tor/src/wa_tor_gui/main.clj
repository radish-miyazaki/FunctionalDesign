(ns wa-tor-gui.main
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [wa-tor.fish :as fish]
   [wa-tor.fish-imp]
   [wa-tor.shark :as shark]
   [wa-tor.water :as water]
   [wa-tor.water-imp]
   [wa-tor.world :as world]
   [wa-tor.world-imp]))

(declare wator)

;; Functions
(defn- setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)
  (-> (world/make 80 80)
      (world/set-cell [40 40] (fish/make))))
(defn- update-state [world]
  (world/tick world))
(defn- draw-state [world]
  (q/background 240)
  (let [cells (::world/cells world)]
    (doseq [loc (keys cells)]
      (let [[x y] loc
            cell (get cells loc)
            x (* x 12)
            y (* y 12)
            color (cond
                    (water/is? cell) [255 255 255]
                    (fish/is? cell) [0 0 255]
                    (shark/is? cell) [255 0 0])]
        (q/no-stroke)
        (apply q/fill color)
        (when-not (water/is? cell)
          (q/rect x y 11 11))))))

(defn ^:export -main [& _args]
  (q/defsketch
    wator
    :title "Wator"
    :size [960 960]
    :setup setup
    :update update-state
    :draw draw-state
    :features [:keep-on-top]
    :middleware [m/fun-mode])
  nil)

