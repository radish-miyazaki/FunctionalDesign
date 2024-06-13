(ns lsp-square.core-spec
  (:require
   [lsp-square.core :refer :all]
   [speclj.core :refer :all]))

(describe "Rectangle"
 (it "calculates proper area after change is size"
  (should= 12 (-> (make-rect 1 1) (set-h 3) (set-w 4) area))
  (should= 25 (area (make-rect 5 5)))
  (should= 18 (perimeter (make-rect 4 5))))

 (it "minimally increases area"
  (should= 15 (-> (make-rect 3 5) minimally-increase-area area))
  (should= 24 (-> (make-rect 4 5) minimally-increase-area area))
  (should= 20 (-> (make-rect 4 4) minimally-increase-area area))))

