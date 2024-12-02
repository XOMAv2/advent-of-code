(ns aoc-2024.day-02
  (:require [aoc.util :as u]
            [better-cond.core :as b]
            [clojure.math :as math]
            [clojure.string :as string]
            [medley.core :as medley]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (comp (partial keep parse-long) (partial re-seq #"\d+")))))

(defn strict
  [report]
  (loop [trend 0
         previous (first report)
         levels (rest report)
         index 0]

    (b/cond
      :let [current (first levels)]

      (and (nil? current)
           (zero? trend))
      :failure

      (nil? current)
      :success

      :let [difference (- current previous)
            delta (abs difference)
            sign (math/signum difference)]

      (and (or (= sign trend)
               (zero? trend))
           (< 0 delta 4))
      (recur sign current (rest levels) (+ index 1))

      :else
      index)))

(defn part1
  [reports]
  (count (filter (comp (partial = :success) strict) reports)))

(defn fuzzy
  [report]
  (let [result (strict report)]
    (case result
      :success true
      :failure false
      (or (= :success (strict (medley/remove-nth (dec result) report)))
          (= :success (strict (medley/remove-nth result report)))
          (= :success (strict (medley/remove-nth (inc result) report)))))))

(defn part2
  [reports]
  (count (filter fuzzy reports)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_02.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [663 692]
    )
  )
