(ns video-store.statement-policy)

(defn- policy-movie-dispatch [policy rental]
  [(:type policy) (-> rental :movie :type)])

(defmulti determine-amount policy-movie-dispatch)
(defmulti determine-points policy-movie-dispatch)
(defmulti total-amount (fn [policy _rentals] (:type policy)))
(defmulti total-points (fn [policy _rentals] (:type policy)))

(defn make-statement-data [policy rental-order]
  (let [{:keys [name]} (:customer rental-order)
        {:keys [rentals]} rental-order]
    {:customer-name name
     :movies (for [rental rentals]
               {:title (:title (:movie rental))
                :price (determine-amount policy rental)})
     :owed (total-amount policy rentals)
     :points (total-points policy rentals)}))

