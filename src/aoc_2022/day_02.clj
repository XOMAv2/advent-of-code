(ns aoc-2022.day-02
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(def char=>shape
  {\A :rock
   \B :paper
   \C :scissors

   \X :rock
   \Y :paper
   \Z :scissors})

(def char=>result
  {\X :loss
   \Y :draw
   \Z :win})

(def shape=>score
  {:rock 1
   :paper 2
   :scissors 3})

(def result=>score
  {:loss 0
   :draw 3
   :win 6})

(def opponent=>you=>result
  {:rock {:rock :draw
          :paper :win
          :scissors :loss}
   :paper {:rock :loss
           :paper :draw
           :scissors :win}
   :scissors {:rock :win
              :paper :loss
              :scissors :draw}})

(def opponent=>result=>you
  (reduce-kv (fn [acc opponent you=>result]
               (reduce-kv (fn [acc you result]
                            (assoc-in acc [opponent result] you))
                          acc
                          you=>result))
             {}
             opponent=>you=>result))

(defn parse-input
  [input]
  (map (fn [[a _ b]]
         [a b])
       (string/split-lines input)))

(defn part1
  [parsed-input]
  (reduce (fn [acc [o-char y-char]]
            (let [opponent (char=>shape o-char)
                  you (char=>shape y-char)
                  result (-> opponent=>you=>result opponent you)]
              (+ acc
                 (shape=>score you)
                 (result=>score result))))
          0
          parsed-input))

(defn part2
  [parsed-input]
  (reduce (fn [acc [o-char r-char]]
            (let [opponent (char=>shape o-char)
                  result (char=>result r-char)
                  you (-> opponent=>result=>you opponent result)]
              (+ acc
                 (shape=>score you)
                 (result=>score result))))
          0
          parsed-input))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_02.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [12794 14979]
    )
  )
