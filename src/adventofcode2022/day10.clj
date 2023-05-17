(ns adventofcode2022.day10
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (fn [line]
              (string/split line #" ")))
       (map (fn [[a b]]
              (cond-> [(keyword a)]
                b (conj (parse-long b)))))))

(defn proceed-dispatcher
  [_state command-vector]
  (first command-vector))

(defmulti proceed
  #'proceed-dispatcher)

(defmethod proceed :addx
  [state [_ amount]]
  (-> state
      (update :cycles conj (:x state))
      (update :cycles conj (:x state))
      (update :x + amount)))

(defmethod proceed :noop
  [state _]
  (update state :cycles conj (:x state)))

(defn part1
  [parsed-input]
  (let [state {:x 1
               :cycles []}
        new-state (reduce proceed state parsed-input)
        {:keys [cycles]} new-state]
    (reduce (fn [signal-strengths-sum cycle-number]
              (let [x-register-value (get cycles (dec cycle-number))
                    signal-strength (* cycle-number x-register-value)]
                (+ signal-strengths-sum signal-strength)))
            0
            [20 60 100 140 180 220])))

(defn part2
  [parsed-input]
  (let [state {:x 1
               :cycles []}
        new-state (reduce proceed state parsed-input)
        {:keys [cycles]} new-state
        crt-width 40
        crt (map-indexed (fn [index x]
                           (if (contains? #{(dec x) x (inc x)}
                                          (mod index crt-width))
                             \#
                             \.))
                         cycles)]
    (->> (partition-all crt-width crt)
         (map (partial apply str))
         (string/join \newline))))

(comment
  (let [input (u/slurp-resource "day10.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [14360 "BGKAEREZ"]
    )
  )
