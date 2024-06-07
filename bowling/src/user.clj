(ns user
  (:require
   [clojure.tools.namespace.repl :refer [refresh]]
   [speclj.core :refer :all]))

(defn reset [] (refresh))

(defn run-all-specs []
  (reset)
  (run-specs))

