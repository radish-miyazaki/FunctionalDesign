(ns abstract-factory-example.shape-factory-implementation
  (:require
   [abstract-factory-example.circle :as circle]
   [abstract-factory-example.shape-factory :as factory]
   [abstract-factory-example.square :as square]))

(defn make []
  {::factory/type ::implementation})

(defmethod factory/make ::implementation [_factory type & args]
  (condp = type
    :square (apply square/make args)
    :circle (apply circle/make args)))

