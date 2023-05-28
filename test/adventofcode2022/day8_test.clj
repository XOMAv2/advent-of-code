(ns adventofcode2022.day8-test
  (:require [adventofcode2022.day8 :as d8]
            [clojure.test :refer :all]))

(def input
  "30373
25512
65332
33549
35390")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d8/parse-input input)
          result (d8/part1 parsed-input)]
      (is (= 21 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d8/parse-input input)
          result (d8/part2 parsed-input)]
      (is (= 8 result)))))
