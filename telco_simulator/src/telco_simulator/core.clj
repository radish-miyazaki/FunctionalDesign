(ns telco-simulator.core)

(def log (atom []))

(defn transition [machine-agent event event-data]
  (swap! log conj (str (:name machine-agent) "<-" event))
  (let [state (:state machine-agent)
        sm (:machine machine-agent)
        result (get-in sm [state event])]
    (if (nil? result)
      (do
        (swap! log conj "TILT!")
        machine-agent)
      (do
        (when (second result)
          ((second result) machine-agent event-data))
        (assoc machine-agent :state (first result))))))

(defn caller-off-hook [_user-agent [telco caller _callee :as call-data]]
  (swap! log conj (:name @caller) " goes off hook.")
  (send telco transition :caller-off-hook call-data))

(defn dial [_user-agent [telco caller _callee :as call-data]]
  (swap! log conj (str (:name @caller) " dials"))
  (send telco transition :dial call-data))

(defn callee-off-hook [_user-agent [telco _caller callee :as call-data]]
  (swap! log conj (str (:name @callee) "goes off hook."))
  (send telco transition :callee-off-hook call-data))

(defn talk [user-agent [telco _caller _callee :as call-data]]
  (swap! log conj (:name user-agent) " talks.")
  (Thread/sleep 10)
  (swap! log conj (str (:name user-agent) " hangs up."))
  (send telco transition :hangup call-data))

(defn dialtone [_telco-agent [_telco caller _callee :as call-data]]
  (swap! log conj (str "dialtone to " (:name @caller)))
  (send caller transition :dialtone call-data))

(defn ring [_telco-agent [_telco caller callee :as call-data]]
  (swap! log conj (str "telco rings " (:name @callee)))
  (send callee transition :ring call-data)
  (send caller transition :ringback call-data))

(defn connect [_telco-agent [_telco caller callee :as call-data]]
  (swap! log conj "telco connects")
  (send caller transition :connected call-data)
  (send callee transition :connected call-data))

(defn disconnect [_telco-agent [_telco caller callee :as call-data]]
  (swap! log conj "disconnect")
  (send caller transition :disconnect call-data)
  (send callee transition :disconnect call-data))

;; State machines
(def user-sm
  {:idle {:call [:calling caller-off-hook]
          :ring [:waiting-for-connection callee-off-hook]
          :disconnect [:idle nil]}
   :calling {:dialtone [:dialing dial]}
   :dialing {:ringback [:waiting-for-connection nil]}
   :waiting-for-connection {:connected [:talking talk]}
   :talking {:disconnect [:idle nil]}})

(def telco-sm
  {:idle {:caller-off-hook [:waiting-for-dial dialtone]
          :hangup [:idle nil]}
   :waiting-for-dial {:dial [:waiting-for-answer ring]}
   :waiting-for-answer {:callee-off-hook
                        [:waiting-for-hangup connect]}
   :waiting-for-hangup {:hangup [:idle disconnect]}})

;; Agent constructors
(defn make-user-agent [name]
  (agent {:state :idle :name name :machine user-sm}))

(defn make-telco-agent [name]
  (agent {:state :idle :name name :machine telco-sm}))

