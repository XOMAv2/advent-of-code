(ns adventofcode2022.day9-test
  (:require [adventofcode2022.day9 :as d9]
            [clojure.test :refer [deftest testing is]]))

(deftest part1-test
  (testing "input from the example"
    (let [input
          "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2"
          parsed-input (d9/parse-input input)
          result (d9/part1 parsed-input)]
      (is (= 13 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [input "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"
          parsed-input (d9/parse-input input)
          result (d9/part2 parsed-input)]
      (is (= 36 result)))))
