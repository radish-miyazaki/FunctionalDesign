(ns visitor-example.json-shape-visitor-implementation
  (:require
   [visitor-example.circle :as circle]
   [visitor-example.json-shape-visitor :as v]
   [visitor-example.square :as square]))

(defmethod v/to-json ::square/square [square]
  (let [{:keys [::square/top-left ::square/side]} square
        [x y] top-left]
    (format "{\"top-left\": [%s,%s], \"side\": %s}" x y side)))

(defmethod v/to-json ::circle/circle [circle]
  (let [{:keys [::circle/center ::circle/radius]} circle
        [x y] center]
    (format "{\"center\": [%s,%s], \"radius\": %s}" x y radius)))

