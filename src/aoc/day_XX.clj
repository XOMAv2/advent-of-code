(ns aoc_20YY.day-XX
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input])

(defn part1
  [parsed-input])

(defn part2
  [parsed-input])

(comment
  (let [input (u/slurp-resource "./aoc_20YY/day_XX.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [... ...]
    )
  )
