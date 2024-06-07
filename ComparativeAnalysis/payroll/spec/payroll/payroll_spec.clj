(ns payroll.payroll-spec
  (:require
   [clojure.spec.alpha :as s]
   [payroll.payroll :refer :all]
   [payroll.payroll_interface :as pi]
   [speclj.core :refer :all])
  (:import
   [java.text SimpleDateFormat]
   [java.util Locale]))

(defn parse-today [date-string]
  (let [sdf (SimpleDateFormat. "MMM dd yyyy" Locale/US)]
    (.parse sdf date-string)))

(describe "payroll"
          (it "pays no one if no one is ready"
              (let [employees []
                    db {:employees employees}
                    today (parse-today "Nov 14 2022")]
                (should (s/valid? ::pi/db db))
                (let [paycheck-directives (payroll today db)]
                  (should (s/valid? ::pi/paycheck-directives paycheck-directives))
                  (should= [] paycheck-directives))))

          (it "pays one salaried employee at end of month by mail"
              (let [employees [{:id "emp1"
                                :schedule :monthly
                                :pay-class [:salaried 5000]
                                :disposition [:mail "name" "home"]}]
                    db {:employees employees}
                    today (parse-today "Nov 30 2021")]
                (should (s/valid? ::pi/db db))
                (let [paycheck-directives (payroll today db)]
                  (should (s/valid? ::pi/paycheck-directives paycheck-directives))
                  (should= [{:type :mail
                             :id "emp1"
                             :name "name"
                             :address "home"
                             :amount 5000}]
                           paycheck-directives))))

          (it "pays one hourly employee on Friday bu Direct Deposit"
              (let [employees [{:id "emp1"
                                :schedule :weekly
                                :pay-class [:hourly 15]
                                :disposition [:deposit "routing" "account"]}]
                    time-cards {"emp1" [["Nov 12 2022" 80/10]]}
                    db {:employees employees :time-cards time-cards}
                    friday (parse-today "Nov 18 2022")]
                (should (s/valid? ::pi/db db))
                (let [paycheck-directives (payroll friday db)]
                  (should (s/valid? ::pi/paycheck-directives paycheck-directives))
                  (should= [{:type :deposit
                             :id "emp1"
                             :routing "routing"
                             :account "account"
                             :amount 120}]
                           paycheck-directives))))

          (it "pays one commissioned employee on an even Friday by Paymaster"
              (let [employees [{:id "emp1"
                                :schedule :biweekly
                                :pay-class [:commissioned 100 5/100]
                                :disposition [:paymaster "paymaster"]}]
                    sales-receipts {"emp1" [["Nov 12 2022" 15000]]}
                    db {:employees employees :sales-receipts sales-receipts}
                    friday (parse-today "Nov 18 2022")]
                (should (s/valid? ::pi/db db))
                (let [paycheck-directives (payroll friday db)]
                  (should (s/valid? ::pi/paycheck-directives paycheck-directives))
                  (should= [{:type :paymaster
                             :id "emp1"
                             :paymaster "paymaster"
                             :amount 850}]
                           paycheck-directives)))))

