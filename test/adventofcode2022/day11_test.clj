(ns adventofcode2022.day11-test
  (:require [adventofcode2022.day11 :as d11]
            [clojure.test :refer [deftest testing is]]))

(def input
  "Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d11/parse-input input)
          result (d11/part1 parsed-input)]
      (is (= 10605 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d11/parse-input input)
          result (d11/part2 parsed-input)]
      (is (= 2713310158 result)))))
