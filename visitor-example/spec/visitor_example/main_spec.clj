(ns visitor-example.main-spec
  (:require
   [speclj.core :refer :all]
   [visitor-example.circle :as circle]
   [visitor-example.json-shape-visitor :as jv]
   [visitor-example.main :refer :all] ; このテストではエントリポイントである main にアクセスしないので、意図的に require している
   [visitor-example.square :as square]))

(describe "shape-visitor"
          (it "makes json square"
              (should= "{\"top-left\": [0,0], \"side\": 1}"
                       (jv/to-json (square/make [0 0] 1))))

          (it "makes json circle"
              (should= "{\"center\": [3,4], \"radius\": 1}"
                       (jv/to-json (circle/make [3 4] 1)))))

