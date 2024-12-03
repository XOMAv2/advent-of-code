(ns aoc-2024.day-03
  (:require [aoc.util :as u]
            [clojure.core.match :refer [match]]
            [clojure.string :as string]))

(defn part1
  [input]
  (reduce (fn [acc [_ a b]]
            (+ acc (* (parse-long a) (parse-long b))))
          0
          (re-seq #"mul\((\d{1,3}),(\d{1,3})\)" input)))

(defn part2
  [input]
  (loop [count? true
         acc 0

         [command & commands]
         (re-seq #"mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)" input)]

    (match [command]
      [nil]
      acc

      [(["do()" & _] :seq)]
      (recur true acc commands)

      [(["don't()" & _] :seq)]

      (recur false acc commands)

      [([(_ :guard #(string/starts-with? % "mul")) a b] :seq)]
      (recur count?
             (cond-> acc
               count? (+ (* (parse-long a) (parse-long b))))
             commands)

      :else
      (recur count? acc commands))))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_03.in")]
    ((juxt part1 part2) input) ; => [157621318 79845780]
    )
  )
