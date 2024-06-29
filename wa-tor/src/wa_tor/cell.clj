(ns wa-tor.cell)

;; Multimethods
(defmulti tick (fn [cell & _args] (::type cell)))

