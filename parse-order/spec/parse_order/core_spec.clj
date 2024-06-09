(ns parse-order.core-spec
  (:require
   [parse-order.core :refer :all]
   [speclj.core :refer [context describe it should=]]))

(describe "Order Entry System"
          (context "Parsing Customers"
            (it "parses a valid customer"
                (should= {:id "1234567"
                          :name "customer name"
                          :address "customer address"
                          :credit-limit 50000}
                         (parse-customer
                          ["Customer-id: 1234567"
                           "Name: customer name"
                           "Address: customer address"
                           "Credit Limit: 50000"])))
            (it "passes inlivaild customers"
                (should= :invalid
                         (parse-customer
                          ["Customer-id: X"
                           "Name: customer name"
                           "Address: customer address"
                           "Credit Limit: 50000"]))
                (should= :invalid
                         (parse-customer
                          ["Customer-id: 1234567"
                           "Name: "
                           "Address: customer address"
                           "Credit Limit: 50000"]))
                (should= :invalid
                         (parse-customer
                          ["Customer-id: 1234567"
                           "Name: customer name"
                           "Address: "
                           "Credit Limit: 50000"]))
                (should= :invalid
                         (parse-customer
                          ["Customer-id: 1234567"
                           "Name: customer name"
                           "Address: customer address"
                           "Credit Limit: invalid"])))
            (it "makes more credit limit is <= 50000"
                (should= :invalid
                         (parse-customer
                          ["Customer-id: 12345678"
                           "Name: customer name"
                           "Address: customer address"
                           "Credit Limit: 50001"])))))

