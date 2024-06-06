(ns payroll.payroll_interface)

(defn get-pay-class [employee]
  (first (:pay-class employee)))

(defn get-dispotion [paycheck-directive]
  (first (:disposition paycheck-directive)))

(defmulti is-today-payday :schedule)
(defmulti calc-pay get-pay-class)
(defmulti dispose get-dispotion)

