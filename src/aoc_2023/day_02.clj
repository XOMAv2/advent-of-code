(ns aoc-2023.day-02
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-subset
  [input]
  (reduce (fn [acc [_ amount color]]
            (update acc
                    (keyword color)
                    (fnil + 0)
                    (parse-long amount)))
          nil
          (re-seq #"(\d+)\s+(\w+)(?:,\s*|$)" input)))

(defn parse-game
  [input]
  (let [[_ id subsets] (re-matches #"Game (\d+): (.+)" input)]
    {:id (parse-long id)
     :subsets (map parse-subset (string/split subsets #"; "))}))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map parse-game)))

(defn part1
  [games]
  (let [limits
        {:red 12
         :green 13
         :blue 14}

        possible?
        (fn [{:keys [red green blue]
              :or {red 0
                   green 0
                   blue 0}}]
          (and (<= red (:red limits))
               (<= green (:green limits))
               (<= blue (:blue limits))))]

    (->> games
         (filter (comp (partial every? possible?) :subsets))
         (reduce (fn [acc {:keys [id] :as _game}]
                   (+ acc id))
                 0))))

(defn part2
  [games]
  (let [calc-max
        (fn [game]
          (reduce (fn [acc {:keys [red green blue]
                            :or {red 0
                                 green 0
                                 blue 0}}]
                    (-> acc
                        (update :red max red)
                        (update :green max green)
                        (update :blue max blue)))
                  {:red 0
                   :green 0
                   :blue 0}
                  (:subsets game)))

        calc-power
        (fn [{:keys [red green blue]
              :or {red 0
                   green 0
                   blue 0}}]
          (* red green blue))]

    (reduce (fn [acc game]
              (+ acc (calc-power (calc-max game))))
            0
            games)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_02.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [2169 60948]
    )
  )
