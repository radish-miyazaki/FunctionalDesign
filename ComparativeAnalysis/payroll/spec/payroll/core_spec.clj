(ns payroll.core-spec
  (:require
   [payroll.core :refer :all]
   [speclj.core :refer :all])
  (:import
   [java.text SimpleDateFormat]))

(defn parse-today [date-string]
  (let [sdf (SimpleDateFormat. "MMM dd yyyy")]
    (.parse sdf date-string)))

(describe "payroll"
 (it "pays one salaried employee at end of month by mail"
     (let [employees [{:id "emp1"
                       :scheudle :monthly
                       :pay-class [:salaried 5000]
                       :disposition [:mail "name" "home"]}]
           db {:emloyees employees}
           today (parse-today "Nov 30 2021")]
       (should= [{:type :mail
                  :id "emp1"
                  :name "name"
                  :address "home"
                  :amount 5000}]
                (payroll today db))))

 (it "pays one hourly employee on Friday bu Direct Deposit"
     (let [employees [{:id "emp1"
                       :schedule :weekly
                       :pay-class [:hourly 12]
                       :disposition [:deposit "routing" "account"]}]
           time-cards {"emp1" [{"Nov 12 2022" 80/10}]}
           db {:employees employees :time-cards time-cards}
           friday (parse-today "Nov 18 2022")]
       (should= [{:type :deposit
                  :id "emp1"
                  :routing "routing"
                  :account "account"
                  :amount 120}]
                (payroll friday db))))

 (it "pays one commissioned employee on an even Friday by Paymaster"
     (let [employees [{:id "emp1"
                       :schedule :biweekly
                       :pay-class [:commisioned 100 5/100]
                       :disposition [:paymaster "paymaster"]}]
           sales-receipts {"emp1" [{"Nov 12 2022" 15000}]}
           db {:employees employees :sales-receipts sales-receipts}
           friday (parse-today "Nov 18 2022")]
       (should= [{:type :paymaster
                  :id "emp1"
                  :paymaster "paymaster"
                  :amount 850}]
                (payroll friday db)))))

