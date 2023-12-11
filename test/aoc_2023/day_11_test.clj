(ns aoc-2023.day-11-test
  (:require [aoc-2023.day-11 :as sut]
            [clojure.test :refer :all]))

(def input
  "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....")

(deftest solve-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)]
      (are [expected multiplier] (= expected (sut/solve parsed-input multiplier))
        374 2
        1030 10
        8410 100))))
