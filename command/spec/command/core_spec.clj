(ns command.core-spec
  (:require
   [command.add-room-command :as ar]
   [command.core :refer :all]
   [speclj.core :refer :all]))

(describe "command"
 (with-stubs)
 (it "executes the command"
  (with-redefs [ar/add-room (stub :add-room {:return :a-room})
                ar/delete-room (stub :delete-room)]
    (gui-app [:add-room-actions :undo-action])
    (should-have-invoked :add-room)
    (should-have-invoked :delete-room {:with [:a-room]}))))
