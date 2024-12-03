(ns aoc-2024.day-03-test
  (:require [aoc-2024.day-03 :as sut]
            [clojure.test :refer :all]))

(deftest part1-test
  (testing "input from the example"
    (let [input "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
          result (sut/part1 input)]
      (is (= 161 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [input "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
          result (sut/part2 input)]
      (is (= 48 result)))))
