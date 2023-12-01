(ns aoc-2022.day-12-test
  (:require [aoc-2022.day-12 :as sut]
            [clojure.test :refer :all]))

(def input
  "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 31 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 29 result)))))
