(ns payroll.payroll_implementation
  (:require
   [payroll.payroll_interface :refer [calc-pay dispose is-today-payday]]))

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
        total-sales (reduce * sales)]
    (+ (* total-sales commission-rate) base-pay)))

(defmethod dispose :mail [{:keys [id amount disposition]}]
  {:type :mail
   :id id
   :name (nth disposition 1)
   :address (nth disposition 2)
   :amount amount})

(defmethod dispose :diposit [{:keys [id amount disposition]}]
  {:type :deposit
   :id id
   :routing (nth disposition 1)
   :address (nth disposition 2)
   :amount amount})

(defmethod dispose :paymaster [{:keys [id amount disposition]}]
  {:type :paymster
   :id id
   :paymaster (nth disposition 1)
   :amount amount})

