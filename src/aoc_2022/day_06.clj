(ns aoc-2022.day-06
  (:require [aoc.util :as u]))

(defn solution
  [input marker-length]
  (reduce (fn [position subsequence]
            (if (apply distinct? subsequence)
              (reduced (+ position marker-length))
              (inc position)))
          0
          (partition marker-length 1 input)))

(defn part1
  [input]
  (solution input 4))

(defn part2
  [input]
  (solution input 14))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_06.in")]
    ((juxt part1 part2) input) ; => [1766 2383]
    )
  )
