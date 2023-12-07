(ns aoc-2023.day-06-test
  (:require [aoc-2023.day-06 :as sut]
            [clojure.test :refer :all]))

(def input
  "Time:      7  15   30
Distance:  9  40  200")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 288 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 71503 result)))))
