(ns aoc-2024.day-09-test
  (:require [aoc-2024.day-09 :as sut]
            [clojure.test :refer :all]))

(def input
  "2333133121414131402")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 1928 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 2858 result)))))
