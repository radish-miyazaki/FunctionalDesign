(ns payroll.payroll
  (:require
   [payroll.payroll_interface :refer [calc-pay dispose is-today-payday]]))

(defn get-employees-to-be-paid-today [today employees]
  (filter #(is-today-payday % today) employees))

(defn- build-employees [db employee]
  (assoc employee :db db))

(defn get-employees [db]
  (map (partial build-employees db) (:employees db)))

(defn create-paycheck-directives [ids payments dispositions]
  (map #(assoc {} :id %1 :amount %2 :disposition %3) ids payments dispositions))

(defn send-paychecks [ids payments dispositions]
  (for [paycheck-directive (create-paycheck-directives ids payments dispositions)]
    (dispose paycheck-directive)))

(defn get-paycheck-amounts [employees]
  (map calc-pay employees))

(defn get-dispositions [employees]
  (map :disposition employees))

(defn get-ids [employees]
  (map :id employees))

(defn payroll [today db]
  (let [employees (get-employees db)
        employees-to-pay (get-employees-to-be-paid-today today employees)
        amounts (get-paycheck-amounts employees-to-pay)
        ids (get-ids employees-to-pay)
        dispositions (get-dispositions employees-to-pay)]
    (send-paychecks ids amounts dispositions)))

