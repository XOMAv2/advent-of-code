(ns aoc-2023.day-11
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(def empty-space
  \.)

(def galaxy
  \#)

(defn parse-input
  [input]
  (let [universe (mapv vec (string/split-lines input))

        rows (count universe)
        columns (count (first universe))

        empty-space? (partial = empty-space)
        galaxy? (partial = galaxy)

        rows-to-be-expand
        (->> (range rows)
             (reduce (fn [acc i]
                       (if (every? empty-space? (get universe i))
                         (conj acc i)
                         acc))
                     '())
             (sort))

        columns-to-be-expand
        (->> (range columns)
             (reduce (fn [acc j]
                       (if (every? empty-space? (map #(get % j) universe))
                         (conj acc j)
                         acc))
                     '())
             (sort))

        galaxies
        (for [i (range rows)
              j (range columns)
              :when (galaxy? (get-in universe [i j]))]
          [i j])

        pairs
        (reduce (fn [acc pair]
                  (conj acc pair))
                #{}
                (for [a galaxies
                      b galaxies
                      :when (not= a b)]
                  #{a b}))]

    {:universe universe
     :rows rows
     :columns columns
     :rows-to-be-expand rows-to-be-expand
     :columns-to-be-expand columns-to-be-expand
     :galaxies galaxies
     :pairs pairs}))

(defn count-expansions
  [from to to-be-expand]
  (loop [[x & xs :as to-be-expand] to-be-expand
         result 0]
    (cond
      (empty? to-be-expand) result
      (>= x to) result
      (> x from) (recur xs (inc result))
      :else (recur xs result))))

(defn solve
  [{:keys [pairs
           rows-to-be-expand
           columns-to-be-expand]}
   multiplier]
  (reduce (fn [acc pair]
            (let [[[ai aj] [bi bj]] (vec pair)

                  [i-min i-max] (if (< ai bi)
                                  [ai bi]
                                  [bi ai])

                  [j-min j-max] (if (< aj bj)
                                  [aj bj]
                                  [bj aj])

                  i-expansions
                  (count-expansions i-min i-max rows-to-be-expand)

                  j-expansions
                  (count-expansions j-min j-max columns-to-be-expand)]

              (+ acc
                 (- i-max i-min i-expansions)
                 (* i-expansions multiplier)
                 (- j-max j-min j-expansions)
                 (* j-expansions multiplier))))
          0
          pairs))

(defn part1
  [parsed-input]
  (solve parsed-input 2))

(defn part2
  [parsed-input]
  (solve parsed-input 1000000))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_11.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [9608724 904633799472]
    )
  )
