(ns adventofcode2022.day6
  (:require [adventofcode2022.util :as u]))

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
  (let [input (u/slurp-resource "day6.in")]
    ((juxt part1 part2) input) ; => [1766 2383]
    )
  )
