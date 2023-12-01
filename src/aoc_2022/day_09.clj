(ns aoc-2022.day-09
  (:require [aoc.util :as u]
            [better-cond.core :as b]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (fn [line]
              (string/split line #" ")))
       (map (fn [[a b]]
              [a (parse-long b)]))))

(defn make-field
  [start]
  (vec (repeat 10 start)))

(defn tail-need-update?
  [[i j :as _head] [a b :as _tail]]
  (or (> (abs (- i a)) 1)
      (> (abs (- j b)) 1)))

(defn get-new-tail
  [[i j :as _head] [a b :as _tail]]
  (let [update-coord (fn [coord delta]
                       (cond-> coord
                         (pos? delta) inc
                         (neg? delta) dec))]
    [(update-coord a (- i a)) (update-coord b (- j b))]))

(defn update-tail
  ([field]
   (update-tail field 0))
  ([field head-index]
   (b/cond
     :let [tail-index (inc head-index)]

     (not (contains? field tail-index))
     field

     :let [head (get field head-index)
           tail (get field tail-index)]

     (tail-need-update? head tail)
     (-> (assoc field tail-index (get-new-tail head tail))
         (recur tail-index))

     :else
     field)))

(defn get-transformation-history
  [field rules]
  (second
   (reduce (fn [[field history] [direction repetition]]
             (reduce (fn [[field history] _]
                       (let [[i j :as _head] (get field 0)
                             new-head (case direction
                                        "U" [(dec i) j]
                                        "R" [i (inc j)]
                                        "D" [(inc i) j]
                                        "L" [i (dec j)])
                             new-field (-> (assoc field 0 new-head)
                                           (update-tail))]
                         [new-field (conj history new-field)]))
                     [field history]
                     (range repetition)))
           [field [field]]
           rules)))

(defn part1
  [parsed-input]
  (->> parsed-input
       (get-transformation-history (make-field [0 0]))
       (map #(get % 1))
       (set)
       (count)))

(defn part2
  [parsed-input]
  (->> parsed-input
       (get-transformation-history (make-field [0 0]))
       (map #(get % 9))
       (set)
       (count)))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_09.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [6745 2793]
    )
  )
