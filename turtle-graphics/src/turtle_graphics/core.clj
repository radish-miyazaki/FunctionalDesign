(ns turtle-graphics.core
  (:require
   [org.clojure/core.async :as async]
   [quil.core :as q]
   [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)
  (let [state {:turtle (turtle/make)
               :channel channel}]
    (async/go
      (turtle-script)
      (prn "Turtle script complete"))
    state))

(defn update-state [state])

(defn draw-state [state])

(defn ^:export -main [& args]
  (q/defsketch turtle-graphics
    :title "Turtle Graphics"
    :size [1000 1000]
    :setup setup
    :update update-state
    :draw draw-state
    :feature [:keep-on-top]
    :middleware [m/fun-mode])
  args)

