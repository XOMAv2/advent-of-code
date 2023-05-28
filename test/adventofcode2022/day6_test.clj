(ns adventofcode2022.day6-test
  (:require [adventofcode2022.day6 :as d6]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (are [result arg] (= result (d6/part1 arg))
      7 "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
      5 "bvwbjplbgvbhsrlpgdmjqwftvncz"
      6 "nppdvjthqldpwncqszvftbrmjlhg"
      10 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
      11 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))

(deftest part2-test
  (testing "input from the example"
    (are [result arg] (= result (d6/part2 arg))
      19 "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
      23 "bvwbjplbgvbhsrlpgdmjqwftvncz"
      23 "nppdvjthqldpwncqszvftbrmjlhg"
      29 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
      26 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")))
