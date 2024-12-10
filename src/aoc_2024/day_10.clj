(ns aoc-2024.day-10
  (:require [aoc.matrix2 :as m2]
            [aoc.util :as u]
            [better-cond.core :as b]))

(defn parse-input
  [input]
  (let [matrix (m2/parse (fn [c]
                           (if-let [digit (parse-long (str c))]
                             digit
                             -1))
                         input)]
    {:matrix matrix
     :rows (count matrix)
     :columns (count (first matrix))}))

(defn find-trails
  ([matrix current-position]
   (find-trails matrix current-position []))
  ([matrix [i j :as current-position] visited]
   (b/cond :let [current-elevation (get-in matrix current-position)]

           (nil? current-elevation)
           []

           :let [last-visited (peek visited)]

           (or (and (nil? last-visited)
                    (zero? current-elevation))
               (and last-visited
                    (= (inc (get-in matrix last-visited)) current-elevation)
                    (not= 9 current-elevation)))
           (let [visited' (conj visited current-position)]
             (u/concatv (find-trails matrix [(inc i) j] visited')
                        (find-trails matrix [(dec i) j] visited')
                        (find-trails matrix [i (inc j)] visited')
                        (find-trails matrix [i (dec j)] visited')))

           (and last-visited
                (= (inc (get-in matrix last-visited)) current-elevation 9))
           [(conj visited current-position)]

           :else
           [])))

(defn part1
  [{:keys [matrix rows columns]}]
  (->> (for [i (range rows)
             j (range columns)
             :when (zero? (get-in matrix [i j]))]
         (->> (find-trails matrix [i j])
              (map peek)
              (distinct)
              (count)))
       (reduce + 0)))

(defn part2
  [{:keys [matrix rows columns]}]
  (->> (for [i (range rows)
             j (range columns)
             :when (zero? (get-in matrix [i j]))]
         (count (find-trails matrix [i j])))
       (reduce + 0)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_10.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [459 1034]
    )
  )
