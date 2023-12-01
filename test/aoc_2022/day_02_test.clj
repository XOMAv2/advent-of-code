(ns aoc-2022.day-02-test
  (:require [aoc-2022.day-02 :as sut]
            [clojure.test :refer :all]))

(def input
  "A Y
B X
C Z")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 15 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 12 result)))))
