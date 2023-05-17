(ns adventofcode2022.day8
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (mapv (fn [row]
          (mapv (fn [column]
                  (parse-long (str column)))
                row))
        (string/split-lines input)))

(defn get-x-neighbors
  [matrix i j]
  [(u/reversev
    (mapv #(nth % j) (subvec matrix 0 i)))
   (subvec (nth matrix i) (inc j))
   (mapv #(nth % j) (subvec matrix (inc i)))
   (u/reversev
    (subvec (nth matrix i) 0 j))])

(defn visible?
  [tree x-neighbors]
  (let [taller-than-neighbor? (fn [neighbor]
                                (> tree neighbor))
        taller-than-everyone? (fn [neighbors]
                                (every? taller-than-neighbor? neighbors))]
    (some taller-than-everyone? x-neighbors)))

(defn get-viewing-distance
  [tree neighbors]
  (reduce (fn [acc neighbor]
            (if (> tree neighbor)
              (inc acc)
              (reduced (inc acc))))
          0
          neighbors))

(defn get-scenic-score
  [tree x-neighbors]
  (reduce (fn [acc neighbors]
            (* acc (get-viewing-distance tree neighbors)))
          1
          x-neighbors))

(defn part1
  [matrix]
  (count (for [i (range (count matrix))
               j (range (count (first matrix)))
               :let [tree (-> matrix (nth i) (nth j))
                     x-neighbors (get-x-neighbors matrix i j)]
               :when (visible? tree x-neighbors)]
           [i j])))

(defn part2
  [matrix]
  (apply max (for [i (range (count matrix))
                   j (range (count (first matrix)))
                   :let [tree (-> matrix (nth i) (nth j))
                         x-neighbors (get-x-neighbors matrix i j)]]
               (get-scenic-score tree x-neighbors))))

(comment
  (let [input (u/slurp-resource "day8.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1676 313200]
    )
  )
