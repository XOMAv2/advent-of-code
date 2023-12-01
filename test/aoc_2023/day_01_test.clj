(ns aoc-2023.day-01-test
  (:require [aoc-2023.day-01 :as sut]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (let [input "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet"
          parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 142 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [input "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"
          parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 281 result)))))
