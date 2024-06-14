(ns video-store.constructors)

(defn make-customer [name]
  {:name name})

(defn make-movie [title type]
  {:title title
   :type type})

(defn make-rental [movie days]
  {:movie movie
   :days days})
 
(defn make-rental-order [customer rentals]
  {:customer customer
   :rentals rentals})

