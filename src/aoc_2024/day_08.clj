(ns aoc-2024.day-08
  (:require [aoc.matrix2 :as m2]
            [aoc.util :as u]
            [clojure.math.combinatorics :as combo]))

(defn parse-input
  [input]
  (let [matrix (m2/parse input)]
    {:rows (count matrix)
     :columns (count (first matrix))

     :antennas
     (reduce-kv (fn [acc i row]
                  (reduce-kv (fn [acc j v]
                               (cond-> acc
                                 (re-matches #"[a-zA-Z0-9]" (str v))
                                 (update v (fnil conj #{}) [i j])))
                             acc
                             row))
                {}
                matrix)}))

(defn within-boundaries?
  [rows columns [i j]]
  (and (< -1 i rows)
       (< -1 j columns)))

(defn part1
  [{:keys [rows columns antennas]}]
  (count
   (reduce-kv (fn [acc _frequency positions]
                (reduce (fn [acc [[i1 j1] [i2 j2]]]
                          (let [di (- i2 i1)
                                dj (- j2 j1)
                                antinode1 [(- i1 di) (- j1 dj)]
                                antinode2 [(+ i2 di) (+ j2 dj)]]
                            (->> [antinode1 antinode2]
                                 (filter (partial within-boundaries? rows columns))
                                 (apply conj acc))))
                        acc
                        (combo/combinations positions 2)))
              #{}
              antennas)))

(defn iterate-while
  [pred fi fj [i j]]
  (->> (map vector (iterate fi i) (iterate fj j))
       (take-while pred)))

(defn part2
  [{:keys [rows columns antennas]}]
  (count
   (reduce-kv (fn [acc _frequency positions]
                (reduce (fn [acc [[i1 j1 :as position1] [i2 j2 :as position2]]]
                          (let [di (- i2 i1)
                                dj (- j2 j1)

                                antinodes1
                                (iterate-while (partial within-boundaries? rows columns)
                                               (partial + (- di))
                                               (partial + (- dj))
                                               position1)

                                antinodes2
                                (iterate-while (partial within-boundaries? rows columns)
                                               (partial + di)
                                               (partial + dj)
                                               position2)]
                            (-> acc
                                (into antinodes1)
                                (into antinodes2))))
                        acc
                        (combo/combinations positions 2)))
              #{}
              antennas)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_08.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [259 927]
    )
  )
