(ns adventofcode2022.day2-test
  (:require [adventofcode2022.day2 :as d2]
            [clojure.test :refer [deftest testing is]]))

(def input
  "A Y
B X
C Z")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d2/parse-input input)
          result (d2/part1 parsed-input)]
      (is (= 15 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d2/parse-input input)
          result (d2/part2 parsed-input)]
      (is (= 12 result)))))
