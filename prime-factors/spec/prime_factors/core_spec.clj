(ns prime-factors.core-spec
  (:require
   [prime-factors.core :refer :all]
   [speclj.core :refer :all]))

(describe "prime factors"
  (it "computes prime factors of integers"
    (should= [] (prime-factors-of 1))
    (should= [2] (prime-factors-of 2))
    (should= [3] (prime-factors-of 3))
    (should= [2 2] (prime-factors-of 4))
    (should= [5] (prime-factors-of 5))
    (should= [2 3] (prime-factors-of 6))
    (should= [7] (prime-factors-of 7))
    (should= [2 2 2] (prime-factors-of 8))
    (should= [3 3] (prime-factors-of 9))))

