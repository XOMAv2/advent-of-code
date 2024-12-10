(ns aoc-2024.day-10-test
  (:require [aoc-2024.day-10 :as sut]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (are [expected input] (= expected (sut/part1 (sut/parse-input input)))
      4 "..90..9
...1.98
...2..7
6543456
765.987
876....
987...."

      3 "10..9..
2...8..
3...7..
4567654
...8..3
...9..2
.....01"

      36 "89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732")))

(deftest part2-test
  (testing "input from the example"
    (are [expected input] (= expected (sut/part2 (sut/parse-input input)))
      3 ".....0.
..4321.
..5..2.
..6543.
..7..4.
..8765.
..9...."

      13 "..90..9
...1.98
...2..7
6543456
765.987
876....
987...."

      227 "012345
123456
234567
345678
4.6789
56789."

      81 "89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732")))
