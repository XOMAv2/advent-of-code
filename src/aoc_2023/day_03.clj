(ns aoc-2023.day-03
  (:require [aoc.util :as u]
            [clojure.string :as string]
            [medley.core :as medley]))

(defn parse-input
  [input]
  (let [matrix (string/split-lines input)]
    {:rows (count matrix)
     :columns (count (first matrix))
     :matrix matrix}))

(defn part1
  [parsed-input]
  (let [{:keys [matrix]} parsed-input
        parts (for [[i row] (map-indexed vector matrix)
                    {:keys [start end group]} (u/re-seq-pos #"\d+" row)
                    :let [di 3 ;; (+ 2 1)
                          dj (+ 2 (- end start))]
                    x (range (dec i) (+ (dec i) di))
                    y (range (dec start) (+ (dec start) dj))
                    :let [c (get-in matrix [x y])]
                    :when c
                    :when (re-find #"[^\d.]" (str c))]
                (parse-long group))]
    (apply + parts)))

(defn part2
  [parsed-input]
  (let [{:keys [matrix]} parsed-input
        potential-gears (for [[i row] (map-indexed vector matrix)
                              {:keys [start end group]} (u/re-seq-pos #"\d+" row)
                              :let [di 3 ;; (+ 2 1)
                                    dj (+ 2 (- end start))]
                              x (range (dec i) (+ (dec i) di))
                              y (range (dec start) (+ (dec start) dj))
                              :let [c (get-in matrix [x y])]
                              :when (= c \*)]
                          [[x y] (parse-long group)])
        gears (->> potential-gears
                   (reduce (fn [acc [pos part]]
                             (update acc pos conj part))
                           {})
                   (medley/filter-vals (fn [parts]
                                         (= (count parts) 2))))]
    (reduce-kv (fn [acc _k v]
                 (+ acc (apply * v)))
               0
               gears)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_03.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [519444 74528807]
    )
  )
