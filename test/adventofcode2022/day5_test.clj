(ns adventofcode2022.day5-test
  (:require [adventofcode2022.day5 :as d5]
            [clojure.test :refer [deftest testing is]]))

(def input
  "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d5/parse-input input)
          result (d5/part1 parsed-input)]
      (is (= "CMZ" result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d5/parse-input input)
          result (d5/part2 parsed-input)]
      (is (= "MCD" result)))))
