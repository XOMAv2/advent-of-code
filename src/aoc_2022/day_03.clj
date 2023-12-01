(ns aoc-2022.day-03
  (:require [aoc.util :as u]
            [clojure.set :as cset]
            [clojure.string :as string]))

(def char=>priority
  (reduce-kv (fn [acc index ch]
               (assoc acc ch (inc index)))
             {}
             (u/concatv (u/char-range \a \z)
                        (u/char-range \A \Z))))

(defn parse-input
  [input]
  (string/split-lines input))

(defn part1
  [parsed-input]
  (reduce (fn [acc [left right]]
            (->> (cset/intersection (set left) (set right))
                 (first)
                 (char=>priority)
                 (+ acc)))
          0
          (map u/split-in-half parsed-input)))

(defn part2
  [parsed-input]
  (reduce (fn [acc lines]
            (->> (map set lines)
                 (apply cset/intersection)
                 (first)
                 (char=>priority)
                 (+ acc)))
          0
          (partition 3 parsed-input)))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_03.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [8493 2552]
    )
  )
