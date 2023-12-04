(ns aoc-2023.day-04
  (:require [aoc.util :as u]
            [clojure.string :as string]
            [clojure.math]))

(defn parse-input
  [input]
  (map (fn [line]
         (let [[_ number winninng ours] (re-matches #"Card\s+(\d+):(.*?)\|(.*)" line)]
           {:number (parse-long number)
            :amount 1
            :winning (set (map parse-long (re-seq #"\d+" winninng)))
            :ours (map parse-long (re-seq #"\d+" ours))}))
       (string/split-lines input)))

(defn part1
  [cards]
  (->> cards
       (map (fn [{:keys [winning ours]}]
              (let [matches (count (filter winning ours))
                    power (dec matches)]
                (if (nat-int? power)
                  (int (clojure.math/pow 2 power))
                  0))))
       (apply +)))

(defn map-n
  [n f coll]
  (map-indexed (fn [index item]
                 (if (< index n)
                   (f item)
                   item))
               coll))

(defn part2
  [cards]
  (let [cards* (loop [visited '()
                      current (first cards)
                      to-visit (rest cards)]
                 (if-not current
                   (reverse visited)
                   (let [{:keys [amount winning ours]} current
                         matches (count (filter winning ours))
                         to-visit* (map-n matches #(update % :amount + amount) to-visit)]
                     (recur (cons current visited)
                            (first to-visit*)
                            (rest to-visit*)))))]
    (reduce (fn [acc {:keys [amount]}]
              (+ acc amount))
            0
            cards*)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_04.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [15205 6189740]
    )
  )
