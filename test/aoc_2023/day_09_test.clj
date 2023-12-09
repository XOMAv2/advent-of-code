(ns aoc-2023.day-09-test
  (:require [aoc-2023.day-09 :as sut]
            [clojure.test :refer :all]))

(def input
  "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 114 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 2 result)))))
