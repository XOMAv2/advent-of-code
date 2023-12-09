(ns aoc-2023.day-09
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (partial re-seq #"-?\d+"))
       (map (partial map parse-long))))

(defn extrapolate-next
  [sq]
  (let [deltas (map (fn [a b]
                      (- b a))
                    sq
                    (rest sq))
        delta (if (every? zero? deltas)
                0
                (extrapolate-next deltas))]
    (+ (last sq) delta)))

(defn extrapolate-prev
  [sq]
  (let [deltas (map (fn [a b]
                      (- b a))
                    sq
                    (rest sq))
        delta (if (every? zero? deltas)
                0
                (extrapolate-prev deltas))]
    (- (first sq) delta)))

(defn part1
  [parsed-input]
  (apply + (map extrapolate-next parsed-input)))

(defn part2
  [parsed-input]
  (apply + (map extrapolate-prev parsed-input)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_09.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1930746032 1154]
    )
  )
