(ns aoc-2023.day-10
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn replace-s
  [matrix [i j :as position]]
  (let [check
        (fn [pos values]
          (when (contains? values (get-in matrix pos))
            pos))

        top (check [(inc i) j] #{\| \L \J})
        bottom (check [(dec i) j] #{\| \7 \F})
        right (check [i (inc j)] #{\- \J \7})
        left (check [i (dec j)] #{\- \L \F})

        replacement
        (cond
          (and top bottom) \|

          (and top right) \F
          (and top left) \7

          (and bottom right) \L
          (and bottom left) \J

          (and right left) \-

          :else (throw (Exception.)))]

    (assoc-in matrix position replacement)))

(defn parse-input
  [input]
  (let [matrix (->> (string/split-lines input)
                    (mapv vec))
        start (u/find-in-matrix matrix \S)]
    {:matrix (replace-s matrix start)
     :rows (count matrix)
     :columns (count (first matrix))
     :start start}))

(defn find-ends
  [matrix [i j :as position]]
  (case (get-in matrix position)
    \| #{[(dec i) j] [(inc i) j]}
    \- #{[i (dec j)] [i (inc j)]}
    \L #{[(dec i) j] [i (inc j)]}
    \J #{[(dec i) j] [i (dec j)]}
    \7 #{[(inc i) j] [i (dec j)]}
    \F #{[(inc i) j] [i (inc j)]}))

(defn find-loop
  [matrix start]
  (loop [path [start]
         previous start
         current (first (find-ends matrix start))]
    (if (= current start)
      path
      (recur (conj path current)
             current
             (first (disj (find-ends matrix current) previous))))))

(defn part1
  [{:keys [matrix start]}]
  (quot (count (find-loop matrix start)) 2))

(defn point-inside?
  [matrix points [i j]]
  (let [edges (->> (for [y (range j)
                         :when (contains? points [i y])]
                     (get-in matrix [i y]))
                   (apply str))]
    (->> edges
         (re-seq #"L\-*7|F\-*J|\|")
         ;; L-J and F-7 should be counted twice when we counting edges
         ;; (but this does not affect the evenness of the result).

         ;; - by itself should be ignored.
         (count)
         (odd?))))

(defn part2
  [{:keys [matrix start columns rows]}]
  (let [path (find-loop matrix start)
        points (set path)]
    (->> (for [i (range rows)
               j (range columns)
               :when (not (contains? points [i j]))
               :when (point-inside? matrix points [i j])]
           [i j])
         (count))))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_10.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [6875 471]
    )
  )
