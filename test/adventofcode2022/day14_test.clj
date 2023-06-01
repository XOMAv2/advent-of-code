(ns adventofcode2022.day14-test
  (:require [adventofcode2022.day14 :as d14]
            [clojure.test :refer :all]))

(def input
  "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d14/parse-input input)
          result (d14/part1 parsed-input)]
      (is (= 24 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d14/parse-input input)
          result (d14/part2 parsed-input)]
      (is (= 93 result)))))
