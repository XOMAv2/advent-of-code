(ns aoc-2024.day-01-test
  (:require [aoc-2024.day-01 :as sut]
            [clojure.test :refer :all]))

(def input
  "3   4
4   3
2   5
1   3
3   9
3   3")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 11 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 31 result)))))
