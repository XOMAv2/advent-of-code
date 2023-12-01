(ns aoc-2022.day-08-test
  (:require [aoc-2022.day-08 :as sut]
            [clojure.test :refer :all]))

(def input
  "30373
25512
65332
33549
35390")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 21 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 8 result)))))
