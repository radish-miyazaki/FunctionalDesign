(ns copy.core-spec
  (:require
   [copy.core :refer :all]
   [speclj.core :refer [describe it should=]]))

(def str-in (atom nil))
(def str-out (atom nil))

(defn str-read []
  (let [c (first @str-in)]
    (if (nil? c)
      :eof
      (do
        (swap! str-in rest)
        c))))

(defn str-write [c]
  (swap! str-out str c)
  str-write)

;; (describe "copy"
;;           (it "can read and write str-read and str-write"
;;               (reset! str-in "abcdef")
;;               (reset! str-out "")
;;               (copy str-read str-write)
;;               (should= "abcdef" @str-out)))

;; (describe "copy"
;;           (it "can read and write str-read and str-write"
;;               (reset! str-in "abcdef")
;;               (reset! str-out "")
;;               (copy {:getchar str-read :putchar str-write})
;;               (should= "abcdef" @str-out)))

;; (describe "copy"
;;           (it "can read and write str-read and str-write"
;;               (let [device {:device-type :test-device
;;                             :input (atom "abcdef")
;;                             :output (atom nil)}]
;;                 (copy device)
;;                 (should= "abcdef" @(:output device)))))

;; (defmethod getchar :test-device [device]
;;   (let [input (:input device)
;;         c (first @input)]
;;     (if (nil? c)
;;       :eof
;;       (do
;;         (swap! input rest)
;;         c))))

;; (defmethod putchar :test-device [device c]
;;   (let [output (:output device)]
;;     (swap! output str c)))

(defrecord str-device [in-atom out-atom]
  device
  (getchar [_]
    (let [c (first @in-atom)]
      (if (nil? c)
        :eof
        (do
          (swap! in-atom rest)
          c))))
  (putchar [_ c]
    (swap! out-atom str c)))

(describe "copy"
          (it "can read and write using str-read and str-write"
              (let [device (->str-device (atom "abcdef") (atom nil))]
                (copy device)
                (should= "abcdef" @(:out-atom device)))))

