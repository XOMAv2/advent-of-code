(ns aoc-2023.day-07
  (:require [aoc.util :as u]
            [better-cond.core :as b]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (map (fn [[_ hand bid]]
         {:hand hand
          :bid (parse-long bid)})
       (re-seq #"(\w+) (\d+)" input)))

(def card->strength
  (->> [\2 \3 \4 \5 \6 \7 \8 \9 \T \J \Q \K \A]
       (map-indexed (fn [index item]
                      [item index]))
       (into {})))

(defn compare-hands
  [card->strength a b]
  (b/cond
    (and (empty? a) (empty? b)) 0
    (empty? a) -1
    (empty? b) 1

    :let [result (compare (card->strength (first a))
                          (card->strength (first b)))]

    (zero? result) (recur card->strength (next a) (next b))
    :else result))

(defn compare-items
  [card->strength a b]
  (cond
    (< (:type a) (:type b)) -1
    (> (:type a) (:type b)) 1
    :else (compare-hands card->strength (:hand a) (:hand b))))

(defn hand->type
  [hand]
  (let [freq (sort > (vals (frequencies hand)))]
    (cond
      (= [5] freq) 6
      (= [4 1] freq) 5
      (= [3 2] freq) 4
      (= [3 1 1] freq) 3
      (= [2 2 1] freq) 2
      (= [2 1 1 1] freq) 1
      (= [1 1 1 1 1] freq) 0
      :else (throw (Exception.)))))

(defn part1
  [parsed-input]
  (->> parsed-input
       (map (fn [{:keys [hand] :as item}]
              (assoc item :type (hand->type hand))))
       (sort (partial compare-items card->strength))
       (map-indexed (fn [index item]
                      (assoc item :rank (inc index))))
       (map (fn [{:keys [bid rank]}]
              (* bid rank)))
       (apply +)))

(def card->strength2
  (->> [\J \2 \3 \4 \5 \6 \7 \8 \9 \T \Q \K \A]
       (map-indexed (fn [index item]
                      [item index]))
       (into {})))

(defn hand->type2
  [hand]
  (let [freq (frequencies hand)
        j-freq (get freq \J 0)
        hand* (if (or (= j-freq 0)
                      (= j-freq 5))
                hand
                (->> (dissoc freq \J)
                     (sort-by second >)
                     (ffirst)
                     (string/replace hand \J)))]
    (hand->type hand*)))

(defn part2
  [parsed-input]
  (->> parsed-input
       (map (fn [{:keys [hand] :as item}]
              (assoc item :type (hand->type2 hand))))
       (sort (partial compare-items card->strength2))
       (map-indexed (fn [index item]
                      (assoc item :rank (inc index))))
       (map (fn [{:keys [bid rank]}]
              (* bid rank)))
       (apply +)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_07.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [251029473 251003917]
    )
  )
