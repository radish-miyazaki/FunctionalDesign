(ns wa-tor.cell)

(defmulti tick (fn [cell & args] (::type cell)))

