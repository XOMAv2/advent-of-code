(ns adventofcode2022.day13
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split input #"\n+")
       (map read-string)))

(declare packet-compare)

(defn vector-compare
  [left right]
  (or (->> (map packet-compare left right)
           (drop-while zero?)
           (first))
      (- (count left) (count right))))

(defn packet-compare
  [left right]
  (cond
    (and (number? left) (number? right))
    (- left right)

    (number? left)
    (recur [left] right)

    (number? right)
    (recur left [right])

    (and (vector? left) (vector? right))
    (vector-compare left right)

    :else
    (throw (Exception.))))

(defn part1
  [parsed-input]
  (->> (partition 2 parsed-input)
       (map-indexed (fn [idx [left right]]
                      [(inc idx) (packet-compare left right)]))
       (keep (fn [[number result]]
               (when (neg? result)
                 number)))
       (apply +)))

(defn part2
  [parsed-input]
  (let [sorted-packets (->> (conj parsed-input [[2]] [[6]])
                            (sort packet-compare))]
    (* (inc (.indexOf sorted-packets [[2]]))
       (inc (.indexOf sorted-packets [[6]])))))

(comment
  (let [input (u/slurp-resource "day13.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [5720 23504]
    )
  )
