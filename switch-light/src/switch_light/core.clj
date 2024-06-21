(ns switch-light.core)

(defmulti turn-on :type)
(defmulti turn-off :type)

(defn turn-on-light []
  ;turn on the bloody light!
  )

(defn turn-off-light []
  ;Criminy! just turn it off!
  )

(defmethod turn-on :light [_switchable]
  (turn-on-light))

(defmethod turn-off :light [_switchable]
  (turn-off-light))

(defn engage-switch [switchable]
  ;Some other stuff...
  (turn-on switchable)
  ;Some more other stuff...
  (turn-off switchable))

