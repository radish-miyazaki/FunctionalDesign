(ns wa-tor.cell)

(defmulti tick (fn [cell & _args] (::type cell)))

