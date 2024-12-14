(ns aoc-2024.day-14-test
  (:require [aoc-2024.day-14 :as sut]
            [clojure.test :refer :all]))

(def input
  "p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input 11 7)
          result (sut/part1 parsed-input)]
      (is (= 12 result)))))
