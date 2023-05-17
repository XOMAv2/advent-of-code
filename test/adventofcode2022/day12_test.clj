(ns adventofcode2022.day12-test
  (:require [adventofcode2022.day12 :as d12]
            [clojure.test :refer [deftest testing is]]))

(def input
  "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d12/parse-input input)
          result (d12/part1 parsed-input)]
      (is (= 31 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d12/parse-input input)
          result (d12/part2 parsed-input)]
      (is (= 29 result)))))
