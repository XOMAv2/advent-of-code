(ns adventofcode2022.day1
  (:require [adventofcode2022.util :as u]
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
  (let [input (u/slurp-resource "day1.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [70369 203002]
    )
  )
