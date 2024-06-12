(ns copy.core)

;; (defn copy [read write]
;;   (let [c (read)]
;;     (if (= c :eof)
;;       nil
;;       (recur read (write c)))))

;; (defn copy [device]
;;   (let [c ((:getchar device))]
;;     (if (= c :eof)
;;       nil
;;       (do
;;         ((:putchar device) c)
;;         (recur device)))))

;; (defmulti getchar (fn [device] (:device-type device)))
;; (defmulti putchar (fn [device _] (:device-type device)))

(defprotocol device
  (getchar [_])
  (putchar [_ c]))

(defn copy [device]
  (let [c (getchar device)]
    (if (= c :eof)
      nil
      (do
        (putchar device c)
        (recur device)))))

