(ns turtle-graphics.core
  (:require
   [core.async :as async]
   [quil.core :as q]
   [quil.middleware :as m]
   [turtle-graphics.turtle :as turtle]))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :rgb)
  (let [state {:turtle (turtle/make)
               :channel channel}]
    (async/go
      (turtle-script)
      (prn "Turtle script complete"))
    state))

(defn handle-commands [channel turtle]
  (loop [turtle turtle]
    (let [command (if (= :idle (:state turtle))
                    (async/poll! channel)
                    nil)]
      (if (nil? command)
        turtle
        (recur (turtle/handle-command turtle command))))))

(defn update-state [{:keys [channel turtle] :as state}]
  (let [turtle (turtle/update-turtle turtle)]
    (assoc state :turtle (handle-commands channel turtle))))

(defn draw-state [state]
  (q/background 240)
  (q/with-translation
    [500 500]
    (let [{:keys [turtle]} state]
      (turtle/draw turtle))))

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

