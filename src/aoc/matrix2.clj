(ns aoc.matrix2
  (:require [clojure.string :as string])
  (:refer-clojure :exclude [find]))

(defn parse
  ([input]
   (parse identity input))
  ([f input]
   (mapv (fn [line]
           (mapv f line))
         (string/split-lines input))))

(defn size
  [matrix]
  [(count matrix) (count (first matrix))])

(defn find-all
  [matrix value]
  (for [[i row] (map-indexed vector matrix)
        [j el] (map-indexed vector row)
        :when (= value el)]
    [i j]))

(defn find
  [matrix value]
  (first (find-all matrix value)))
