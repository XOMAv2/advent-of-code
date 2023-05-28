(ns adventofcode2022.day4-test
  (:require [adventofcode2022.day4 :as d4]
            [clojure.test :refer :all]))

(def input
  "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d4/parse-input input)
          result (d4/part1 parsed-input)]
      (is (= 2 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d4/parse-input input)
          result (d4/part2 parsed-input)]
      (is (= 4 result)))))
