(ns video-store.order-processing
  (:require
   [video-stoer.statement-policy :refer :all]
   [video-store.statement-formatter :refer :all]))

(defn process-order [policy formatter order]
  (->> order
       (make-statement-data policy)
       (format-rental-statement formatter)))

