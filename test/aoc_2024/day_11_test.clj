(ns aoc-2024.day-11-test
  (:require [aoc-2024.day-11 :as sut]
            [clojure.test :refer :all]))

(def input
  "125 17")

(deftest part1-test
  (testing "make-step-seq"
    (are [expected blinks stones] (= expected (sut/iterate-reduce blinks sut/make-step-seq stones))
      '(1 2024 1 0 9 9 2021976) 1 '(0 1 10 99 999)
      '(253000 1 7) 1 '(125 17)
      '(253 0 2024 14168) 2 '(125 17)
      '(512072 1 20 24 28676032) 3 '(125 17)
      '(512 72 2024 2 0 2 4 2867 6032) 4 '(125 17)
      '(1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32) 5 '(125 17)
      '(2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2) 6 '(125 17)))

  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 55312 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 65601038650482 result)))))
