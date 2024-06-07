(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.tools.namespace.repl :refer [refresh]]
   [speclj.core :refer :all]))

(defn load-specs []
  (doseq [file (file-seq (io/file "spec"))]
    (when (and (.isFile file)
               (str/ends-with? (.getName file) ".clj"))
      (println "Loading spec file:" (.getPath file))
      (load-file (.getPath file)))))

(defn reset [] (refresh))

(defn run-all-specs []
  (load-specs)
  (reset)
  (run-specs))

