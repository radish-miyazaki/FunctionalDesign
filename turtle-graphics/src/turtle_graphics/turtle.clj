(ns turtle-graphics.turtle
  (:require
   [clojure.spec.alpha :as s]
   [quil.core :as q]))

(s/def ::position (s/tuple number? number?)) ; 現在の亀の位置
(s/def ::heading (s/and number? #(<= 0 % 360))) ; 現在の亀の向き
(s/def ::speed (s/and int? pos?)) ; 亀の移動速度
(s/def ::velocity number?) ; 亀の速度（/step）
(s/def ::distance number?) ; 亀が直進（後進）する距離
(s/def ::omega number?) ; 亀の角速度（/step）
(s/def ::angle number?) ; 亀が回転する角度
(s/def ::weight (s/and pos? number?)) ; 線の太さ
(s/def ::state #{:idle :busy})
(s/def ::pen #{:up :down})
(s/def ::pen-start
  (s/or :nil nil?
        :pos (s/tuple number? number?)))
(s/def ::line-start (s/tuple number? number?))
(s/def ::line-end (s/tuple number? number?))
(s/def ::line (s/keys :req-un [::line-start ::lineend]))
(s/def ::lines (s/coll-of ::line))
(s/def ::visible boolean?)
(s/def ::turtle (s/keys :req-un [::position
                                 ::heading
                                 ::velocity
                                 ::distance
                                 ::omega
                                 ::angle
                                 ::pen
                                 ::weight
                                 ::speed
                                 ::lines
                                 ::visible
                                 ::state]
                        :opt-un [::pen-start]))

(defn make []
  {:post [(s/assert ::turtle %)]}
  {:position [0 0]
   :heading 0.0
   :velocity 0.0
   :distance 0.0
   :omega 0.0
   :angle 0.0
   :pen :up
   :weight 1
   :speed 5
   :visible true
   :lines []
   :state :idle})

(def WIDTH 10)
(def HEIGHT 15)

(defn draw [turtle]
  ;; 現在の線を描画
  (when (= :down (:pen turtle))
    (q/stroke 0)
    (q/stroke-weight (:weight turtle))
    (q/line (:pen-start turtle) (:position turtle)))

  ;; 以前の線を描画
  (doseq [line (:lines turtle)]
    (q/stroke-weight (:line-weight line))
    (q/line (:pen-start line) (:line-end line)))

  ;; 亀を描画
  (when (:visible turtle)
    (q/stroke-weight 1)
    (let [[x y] (:position turtle)
          heading (q/radians (:heading turtle))
          base-left (- (/ WIDTH 2))
          base-right (- (/ WIDTH 2))
          tip HEIGHT]
      (q/stroke 0)
      (q/with-translation [x y]
        (q/with-rotation
          [heading]
          (q/line 0 base-left 0 base-right)
          (q/line 0 base-left tip 0)
          (q/line 0 base-right tip 0))))))

(defn update-position
  "亀の位置を更新する関数"
  [{:keys [position velocity heading distance] :as turtle}]
  (let [step (min (q/abs velocity) distance)
        distance (- distance step)
        step (if (neg? velocity) (- step) step)
        radians (q/radians heading)
        [x y] position
        vx (* step (Math/cos radians))
        vy (* step (Math/sin radians))
        position [(+ x vx) (+ y vy)]]
    (assoc turtle :position position
           :distance distance
           :velocity (if (zero? distance) 0.0 velocity))))

(defn update-heading
  "亀の向きを更新する関数"
  [{:keys [heading omega angle] :as turtle}]
  (let [angle-step (min (q/abs omega) angle)
        angle (- angle angle-step)
        angle-step (if (neg? omega) (- angle-step) angle-step)
        heading (mod (+ heading angle-step) 360)]
    (assoc turtle :heading heading
           :angle angle
           :omega (if (zero? angle) 0.0 omega))))

(defn make-line [{:keys [pen-start position weight]}]
  {:line-start pen-start
   :line-end position
   :line-weight weight})

(defn update-turtle [turtle]
  {:post [(s/assert ::turtle %)]}
  (if (= :idle (:state turtle))
    turtle
    (let [{:keys [distance
                  state
                  angle
                  lines
                  position
                  pen
                  pen-start] :as turtle}
          (-> turtle
              (update-position)
              (update-heading))
          done? (and (zero? distance) (zero? angle))
          state (if done? :idle state)
          lines (if (and done? (= pen :down))
                  (conj lines (make-line turtle))
                  lines)
          pen-start (if (and done? (= pen :down))
                      position
                      pen-start)]
      (assoc turtle :state state
             :lines lines
             :pen-start pen-start))))

(defn pen-down [{:keys [position pen-start pen] :as turtle}]
  (assoc turtle :pen :down
         :pen-start (if (= :up pen) position pen-start)))

(defn pen-up [{:keys [pen lines] :as turtle}]
  (if (= :up pen)
    turtle
    (let [new-line (make-line turtle)
          lines (conj lines new-line)]
      (assoc turtle :pen :up
             :lines lines
             :pen-start nil))))

(defn forward [turtle [distance]]
  (assoc turtle :velocity (:speed turtle)
         :distance distance
         :state :busy))

(defn back [turtle [distance]]
  (assoc turtle :velocity (- (:speed turtle))
         :distance distance
         :state :busy))

(defn right [turtle [angle]]
  (assoc turtle
         :omega (* 2 (:speed turtle)) ; 回転速度は移動速度の 2 倍とする
         :angle angle
         :state :busy))

(defn left [turtle [angle]]
  (assoc turtle
         :omega (* -2 (:speed turtle))
         :angle angle
         :state :busy))

(defn hide [turtle]
  (assoc turtle :visible false))

(defn show [turtle]
  (assoc turtle :visible true))

(defn weight [turtle [weight]]
  (assoc turtle :weight weight))

(defn speed [turtle [speed]]
  (assoc turtle :speed speed))

(defn handle-command [turtle [cmd & args]]
  (condp = cmd
    :forward (forward turtle args)
    :back (back turtle args)
    :right (right turtle args)
    :left (left turtle args)
    :pen-down (pen-down turtle)
    :pen-up (pen-up turtle)
    :hide (hide turtle)
    :show (show turtle)
    :weight (weight turtle args)
    :speed (speed turtle args)
    :else turtle))

