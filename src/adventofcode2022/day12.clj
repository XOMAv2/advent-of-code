(ns adventofcode2022.day12
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (mapv vec)))

(defn replace-char
  [ch]
  (case ch
    \S \a
    \E \z
    ch))

(defn char-compare
  [a b]
  (compare (replace-char a) (replace-char b)))

(defn less-or-equal?
  [a b]
  (>= (char-compare a b) -1))

(defn greater-or-equal?
  [a b]
  (<= (char-compare a b) 1))

(defn get-possible-steps
  [suitability-fn matrix position]
  (let [[i j] position
        value (get-in matrix [i j])

        up [(dec i) j]
        right [i (inc j)]
        down [(inc i) j]
        left [i (dec j)]

        suitable?
        (fn [position]
          (some->> (get-in matrix position)
                   (suitability-fn value)))]
    
    (filterv suitable? [up right down left])))

(defn part1
  [matrix]
  (let [S-position (u/find-in-matrix matrix \S)
        E-position (u/find-in-matrix matrix \E)]
    (-> matrix
        (u/bfs (partial get-possible-steps less-or-equal?) S-position #{E-position})
        (count)
        (dec))))

(defn part2
  [matrix]
  (let [E-position (u/find-in-matrix matrix \E)
        finish? (fn [position]
                  (= \a (get-in matrix position)))]
    (-> matrix
        (u/bfs (partial get-possible-steps greater-or-equal?) E-position finish?)
        (count)
        (dec))))

(comment
  (let [input (u/slurp-resource "day12.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [408 399]
    )
  )
