(ns aoc-2023.day-06
  (:require [aoc.util :as u]))

(defn parse-input
  [input]
  (let [[_  time distance]
        (re-matches #"(?s)Time:(.*?)Distance:(.*?)" input)

        ->longs
        (comp (partial map parse-long) (partial re-seq #"\d+"))

        ->long
        (comp parse-long (partial apply str) (partial re-seq #"\d+"))]

    {:multi (map (fn [t d]
                   {:time t
                    :distance d})
                 (->longs time)
                 (->longs distance))
     :single {:time (->long time)
              :distance (->long distance)}}))

(defn play
  [{:keys [time distance] :as _race}]
  (loop [x 1
         result 0]
    (cond (>= x time)
          result

          (> (* x (- time x)) distance)
          (recur (inc x) (inc result))

          :else
          (recur (inc x) result))))

(defn part1
  [{:keys [multi]}]
  (apply * (map play multi)))

(defn part2
  [{:keys [single]}]
  (play single))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_06.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [440000 26187338]
    )
  )
