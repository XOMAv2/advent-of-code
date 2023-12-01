(ns aoc-2022.day-01-test
  (:require [aoc-2022.day-01 :as sut]
            [clojure.test :refer :all]))

(def input
  "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part1 parsed-input)]
      (is (= 24000 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (sut/parse-input input)
          result (sut/part2 parsed-input)]
      (is (= 45000 result)))))
