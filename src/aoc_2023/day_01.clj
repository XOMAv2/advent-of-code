(ns aoc-2023.day-01
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (string/split-lines input))

(defn part1
  [parsed-input]
  (let [line->digits
        (fn [line]
          (reduce (fn [acc c]
                    (let [mapping {\1 1
                                   \2 2
                                   \3 3
                                   \4 4
                                   \5 5
                                   \6 6
                                   \7 7
                                   \8 8
                                   \9 9}]
                      (if-some [digit (get mapping c)]
                        (conj acc digit)
                        acc)))
                  []
                  line))]

    (->> (map line->digits parsed-input)
         (map #(+ (* (first %) 10) (last %)))
         (reduce +))))

(defn part2
  [parsed-input]
  (let [substrings
        (fn [s]
          (->> (seq s)
               (iterate next)
               (take-while some?)
               (map (partial apply str))))

        line->digits
        (fn [line]
          (reduce (fn [acc sub]
                    (let [mapping {"1" 1
                                   "2" 2
                                   "3" 3
                                   "4" 4
                                   "5" 5
                                   "6" 6
                                   "7" 7
                                   "8" 8
                                   "9" 9
                                   "one" 1
                                   "two" 2
                                   "three" 3
                                   "four" 4
                                   "five" 5
                                   "six" 6
                                   "seven" 7
                                   "eight" 8
                                   "nine" 9}]
                      (if-some [digit (some (fn [x]
                                              (when (string/starts-with? sub x)
                                                (get mapping x)))
                                            (keys mapping))]
                        (conj acc digit)
                        acc)))
                  []
                  (substrings line)))]
    (->> (map line->digits parsed-input)
         (map #(+ (* (first %) 10) (last %)))
         (reduce +))))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_01.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [56465 55902]
    )
  )
