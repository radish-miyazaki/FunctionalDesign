(ns command.undouble-command)

(defmulti execute :type)
(defmulti undo :type)

(defn my-reduce [coll f acc]
  (if (empty? coll)
    acc
    (recur (rest coll) f (f acc (first coll)))))

