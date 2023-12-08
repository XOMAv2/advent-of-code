(ns aoc-2023.day-08-test
  (:require [aoc-2023.day-08 :as sut]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (let [input-1 "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)"
          parsed-input-1 (sut/parse-input input-1)
          result-1 (sut/part1 parsed-input-1)

          input-2 "LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)"
          parsed-input-2 (sut/parse-input input-2)
          result-2 (sut/part1 parsed-input-2)]
      (is (= 2 result-1)
          (= 6 result-2)))))

(deftest part2-test
  (testing "input from the example"
    (let [input "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)"
          parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 6 result)))))
