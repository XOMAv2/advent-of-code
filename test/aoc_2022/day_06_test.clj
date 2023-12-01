(ns aoc-2022.day-06-test
  (:require [aoc-2022.day-06 :as sut]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (are [result arg] (= result (sut/part1 arg))
      7 "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
      5 "bvwbjplbgvbhsrlpgdmjqwftvncz"
      6 "nppdvjthqldpwncqszvftbrmjlhg"
      10 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
      11 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))

(deftest part2-test
  (testing "input from the example"
    (are [result arg] (= result (sut/part2 arg))
      19 "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
      23 "bvwbjplbgvbhsrlpgdmjqwftvncz"
      23 "nppdvjthqldpwncqszvftbrmjlhg"
      29 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
      26 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
