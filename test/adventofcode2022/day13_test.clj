(ns adventofcode2022.day13-test
  (:require [adventofcode2022.day13 :as d13]
            [clojure.test :refer :all]))

(def input
  "[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]")

(deftest test-vector-compare
  (are [is-right-order left right] (= is-right-order (neg? (d13/vector-compare left right)))
    false [[]] []
    true [] [3]
    false [9] [8 7 6]
    true [2 3 4] [4]
    false [7 7 7 7] [7 7 7]))

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d13/parse-input input)
          result (d13/part1 parsed-input)]
      (is (= 13 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d13/parse-input input)
          result (d13/part2 parsed-input)]
      (is (= 140 result)))))
