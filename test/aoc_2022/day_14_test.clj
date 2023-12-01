(ns aoc-2022.day-14-test
  (:require [aoc-2022.day-14 :as sut]
            [clojure.test :refer :all]))

(def input
  "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 24 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 93 result)))))
