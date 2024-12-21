(ns aoc-2024.day-20-test
  (:require [aoc-2024.day-20 :as sut]
            [clojure.test :refer :all]
            [medley.core :as medley]))

(def input
  "###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############")

(deftest part1-test
  (testing "input from the example"
    (let [result (->> (sut/parse-input input)
                      (sut/build-path)
                      (sut/calc-cheat-frequencies 2))]
      (is (= {64 1
              40 1
              38 1
              36 1
              20 1
              12 3
              10 2
              8 4
              6 2
              4 14
              2 14} result)))))

(deftest part2-test
  (testing "input from the example"
    (let [result (->> (sut/parse-input input)
                      (sut/build-path)
                      (sut/calc-cheat-frequencies 20)
                      (medley/filter-keys (partial < 49)))]
      (is (= {50 32
              52 31
              54 29
              56 39
              58 25
              60 23
              62 20
              64 19
              66 12
              68 14
              70 12
              72 22
              74 4
              76 3} result)))))
