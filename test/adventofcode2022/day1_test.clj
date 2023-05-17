(ns adventofcode2022.day1-test
  (:require [adventofcode2022.day1 :as d1]
            [clojure.test :refer [deftest testing is]]))

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
    (let [parsed-input (d1/parse-input input)
          result (d1/part1 parsed-input)]
      (is (= 24000 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d1/parse-input input)
          result (d1/part2 parsed-input)]
      (is (= 45000 result)))))
