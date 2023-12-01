(ns aoc-2022.day-01
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split input #"\n\n")
       (map #(string/split % #"\n"))
       (map #(map parse-long %))))

(defn part1
  [parsed-input]
  (->> (map u/sum parsed-input)
       (apply max)))

(defn part2
  [parsed-input]
  (->> (map u/sum parsed-input)
       (sort >)
       (take 3)
       (apply +)))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_01.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [70369 203002]
    )
  )
