(ns abstract-factory-example.shape-factory)

(defmulti make
  (fn [factory _type & _args] (::type factory)))

