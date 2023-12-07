(ns aoc-2023.day-07-test
  (:require [aoc-2023.day-07 :as sut]
            [clojure.test :refer :all]))

(def input
  "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 6440 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 5905 result)))))
