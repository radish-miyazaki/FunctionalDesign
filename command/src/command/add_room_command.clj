(ns command.add-room-command
  (:require
   [command.undouble-command :as uc]))

(defn add-room []
  ;stuff that adds rooms to the canvas
  ;add returns the added room
  )

(defn delete-room [_room]
  ;stuff that deletes the specified room from the canvas
  )

(defn make-add-room-command []
  {:type :add-room-room})

(defmethod uc/execute :add-room-command [_cmd]
  (assoc (make-add-room-command) :the-added-room (add-room)))

(defmethod uc/undo :add-room-command [_cmd]
  (delete-room (:the-added-room _cmd)))

