(ns aoc-2024.day-12-test
  (:require [aoc-2024.day-12 :as sut]
            [clojure.test :refer :all]))

(def input1
  "AAAA
BBCD
BBCC
EEEC")

(def input2
  "OOOOO
OXOXO
OOOOO
OXOXO
OOOOO")

(def input3
  "RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE")

(deftest part1-test
  (testing "input from the example"
    (are [expected input] (= expected (sut/part1 (sut/parse-input input)))
      140 input1
      772 input2
      1930 input3)))

(deftest part2-test
  (testing "input from the example"
    (are [expected input] (= expected (sut/part2 (sut/parse-input input)))
      80 input1
      436 input2
      1206 input3

      236 "EEEEE
EXXXX
EEEEE
EXXXX
EEEEE"

      368 "AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA")))
