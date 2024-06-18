(ns video-store.quick-check
  (:require
   [clojure.spec.alpha :as s]
   [clojure.test.check :as tc]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [speclj.core :refer :all]
   [video-store.buy-two-get-one-free-policy :refer :all]
   [video-store.constructors :as constructors :refer :all]
   [video-store.normal-statement-policy :refer :all]
   [video-store.statement-policy :as policy :refer :all]))

(def gen-customer-name
  (gen/such-that not-empty gen/string-alphanumeric))
(def gen-customer
  (gen/fmap (fn [name] {:name name}) gen-customer-name))
(def gen-days (gen/elements (range 1 100)))
(def gen-movie-type
  (gen/elements [:regular :childrens :new-release]))
(def gen-movie
  (gen/fmap (fn [[title type]] {:title title :type type})
            (gen/tuple gen/string-alphanumeric gen-movie-type)))
(def gen-rental
  (gen/fmap (fn [[movie days]] {:movie movie :days days})
            (gen/tuple gen-movie gen-days)))
(def gen-rentals
  (gen/such-that not-empty (gen/vector gen-rental)))
(def gen-rental-order
  (gen/fmap (fn [[customer rentals]] {:customer customer :rentals rentals})
              (gen/tuple gen-customer gen-rentals)))
(def gen-policy (gen/elements
                 [(make-normal-policy) (make-buy-two-get-one-free-policy)]))

(describe "Quick check statement policy"
 (it "generates valid rental orders"
  (should-be
   :result
   (tc/quick-check
    100
    (prop/for-all
     [rental-order gen-rental-order]
     (nil?
      (s/explain-data
       ::constructors/rental-order
       rental-order))))))

 (it "produces valid statement data"
  (should-be
   :result
   (tc/quick-check
    100
    (prop/for-all
     [rental-order gen-rental-order
      policy gen-policy]
     (nil?
      (s/explain-data
       ::policy/statement-data
       (make-statement-data policy rental-order)
       ))))))

 (it "statement data totals are consistent under all policies"
   (should-be
    :result
    (tc/quick-check
     100
     (prop/for-all
      [rental-order gen-rental-order]
      (let [policy (make-normal-policy)
            statement-data (make-statement-data policy rental-order)
            prices (map :price (:movies statement-data))
            owed (:owed statement-data)]
        (= owed (reduce + prices))))))))

