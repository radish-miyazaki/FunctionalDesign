(ns payroll.payroll_interface
  (:require
   [clojure.spec.alpha :as s]))

(defn get-pay-class [employee]
  (first (:pay-class employee)))

(defn get-dispotion [paycheck-directive]
  (first (:disposition paycheck-directive)))

(defmulti is-today-payday :schedule)
(defmulti calc-pay get-pay-class)
(defmulti dispose get-dispotion)

(defn- is-last-day-of-month [_] true)

(defmethod is-today-payday :monthly [_ today]
  (is-last-day-of-month today))

(defn- is-friday [_] true)

(defmethod is-today-payday :weekly [_ today]
  (is-friday today))

(defn- is-even-friday [_] true)

(defmethod is-today-payday :biweekly [_ today]
  (is-even-friday today))

(defn- get-salary [employee]
  (second (:pay-class employee)))

(defmethod calc-pay :salaried [employee]
  (get-salary employee))

(defmethod calc-pay :hourly [employee]
  (let [db (:db employee)
        time-cards (:time-cards db)
        my-time-cards (get time-cards (:id employee))
        [_ hourly-rate] (:pay-class employee)
        hours (map second my-time-cards)
        total-hours (reduce + hours)]
    (* total-hours hourly-rate)))

(defmethod calc-pay :commissioned [employee]
  (let [db (:db employee)
        sales-receipts (:sales-receipts db)
        my-sales-receipts (get sales-receipts (:id employee))
        [_ base-pay commission-rate] (:pay-class employee)
        sales (map second my-sales-receipts)
        total-sales (reduce + sales)]
    (+ (* total-sales commission-rate) base-pay)))

(defmethod dispose :mail [{:keys [id amount disposition]}]
  {:type :mail
   :id id
   :name (nth disposition 1)
   :address (nth disposition 2)
   :amount amount})

(defmethod dispose :deposit [{:keys [id amount disposition]}]
  {:type :deposit
   :id id
   :routing (nth disposition 1)
   :account (nth disposition 2)
   :amount amount})

(defmethod dispose :paymaster [{:keys [id amount disposition]}]
  {:type :paymaster
   :id id
   :paymaster (nth disposition 1)
   :amount amount})

(s/def ::id string?)
(s/def ::schedule #{:monthly :weekly :biweekly})

(s/def ::salaries-pay-class (s/tuple #(= % :salaried) pos?))
(s/def ::hourly-pay-class (s/tuple #(= % :hourly) pos?))
(s/def ::commissioned-pay-class (s/tuple #(= % :commissioned) pos? pos?))
(s/def ::pay-class (s/or :salaried ::salaries-pay-class
                         :hourly ::hourly-pay-class
                         :commissioned ::commissioned-pay-class))

(s/def ::mail-disposition (s/tuple #(= % :mail) string? string?))
(s/def ::deposit-disposition (s/tuple #(= % :deposit) string? string?))
(s/def ::paymaster-disposition (s/tuple #(= % :paymaster) string?))
(s/def ::disposition (s/or ::mail ::mail-disposition
                          ::deposit ::deposit-disposition
                          ::paymaster ::paymaster-disposition))

(s/def ::employee (s/keys :req-un [::id ::pay-class ::schedule ::disposition]))
(s/def ::employees (s/coll-of ::employee))

(s/def ::date string?)
(s/def ::time-card (s/tuple ::date pos?))
(s/def ::time-cards (s/map-of ::id (s/coll-of ::time-card)))

(s/def ::sales-receipt (s/tuple ::date pos?))
(s/def ::sales-receipts (s/map-of ::id (s/coll-of ::sales-receipt)))

(s/def ::db (s/keys :req-un [::employees]
                    :opt-un [::time-cards ::sales-receipts]))

(s/def ::amount pos?)
(s/def ::name string?)
(s/def ::address string?)
(s/def ::mail-directive (s/and #(= (:type %) :mail)
                               (s/keys :req-un [::id
                                                ::name
                                                ::address
                                                ::amount])))

(s/def ::routing string?)
(s/def ::account string?)
(s/def ::deposit-directive (s/and #(= (:type %) :deposit)
                                  (s/keys :req-un [::id ::routing ::account ::amount])))

(s/def ::paymaster string?)
(s/def ::paymaster-directive (s/and #(= (:type %) :paymaster)
                                    (s/keys :req-un [::id ::paymaster ::amount])))

(s/def ::paycheck-directive (s/or
                             :mail ::mail-directive
                             :deposit ::deposit-directive
                             :paymaster ::paymaster-directive))
(s/def ::paycheck-directives (s/coll-of ::paycheck-directive))

