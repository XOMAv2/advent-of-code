(ns aoc-2024.day-04
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (mapv (fn [line]
            (mapv identity line))
          (string/split-lines input)))

(defn find-xmas
  [matrix x y dx dy]
  (let [xmas "XMAS"]
    (loop [word \X
           i (+ x dx)
           j (+ y dy)]
      (if-let [letter (get-in matrix [i j])]
        (let [new-word (str word letter)]
          (cond
            (= xmas new-word)
            true

            (string/starts-with? xmas new-word)
            (recur new-word (+ i dx) (+ j dy))

            :else
            false))
        false))))

(defn part1
  [matrix]
  (apply + (for [i (range (count matrix))
                 j (range (count (first matrix)))
                 :when (= \X (get-in matrix [i j]))
                 dx [-1 0 1]
                 dy [-1 0 1]
                 :when (not= 0 dx dy)
                 :when (find-xmas matrix i j dx dy)]
             1)))

(defn find-x-mas
  [matrix i j]
  (let [nth2
        (fn [x y]
          (get-in matrix [x y]))]

    (and (= \A (nth2 i j))

         (let [ne (nth2 (inc i) (inc j))
               sw (nth2 (dec i) (dec j))]
           (or (and (= \M ne) (= \S sw))
               (and (= \S ne) (= \M sw))))

         (let [nw (nth2 (dec i) (inc j))
               se (nth2 (inc i) (dec j))]
           (or (and (= \M nw) (= \S se))
               (and (= \S nw) (= \M se)))))))

(defn part2
  [matrix]
  (apply + (for [i (range (count matrix))
                 j (range (count (first matrix)))
                 :when (find-x-mas matrix i j)]
             1)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_04.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [2613 1905]
    )
  )
