(ns aoc-2024.day-01
  (:require [aoc.util :as u]))

(defn parse-input
  [input]
  (->> (re-seq #"\d+" input)
       (keep parse-long)
       (partition 2)
       (reduce (fn [acc [left right]]
                 (-> acc
                     (update :left conj left)
                     (update :right conj right)))
               {:left []
                :right []})))

(defn part1
  [{:keys [left right]}]
  (reduce (fn [acc [l r]]
            (+ acc (abs (- r l))))
          0
          (map vector
               (sort left)
               (sort right))))

(defn part2
  [{:keys [left right]}]
  (let [freq (frequencies right)]
    (reduce (fn [acc l]
              (+ acc (* l (get freq l 0))))
            0
            left)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_01.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1388114 23529853]
    )
  )
