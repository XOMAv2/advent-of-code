(ns adventofcode2022.day3-test
  (:require [adventofcode2022.day3 :as d3]
            [clojure.test :refer :all]))

(def input
  "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d3/parse-input input)
          result (d3/part1 parsed-input)]
      (is (= 157 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d3/parse-input input)
          result (d3/part2 parsed-input)]
      (is (= 70 result)))))

