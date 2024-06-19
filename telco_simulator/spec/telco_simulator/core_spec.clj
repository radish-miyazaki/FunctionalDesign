(ns telco-simulator.core-spec
  (:require
   [speclj.core :refer :all]
   [telco-simulator.core :refer :all]))

(describe "telco"
 (it "should make and receive call"
   (let [caller (make-user-agent "Bob")
         callee (make-user-agent "Alice")
         telco (make-telco-agent "telco")]
     (reset! log [])
     (send caller transition :call [telco caller callee])
     (Thread/sleep 100)
     (prn @log)
     (should= :idle (:state @caller))
     (should= :idle (:state @callee))
     (should= :idle (:state @telco)))))

