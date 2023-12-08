(ns aoc-2023.day-08
  (:require [aoc.util :as u]))

(defn parse-input
  [input]
  (let [instructions
        (re-find #"\w+" input)

        graph
        (reduce (fn [acc [_ node left right]]
                  (assoc acc node {:left left
                                   :right right}))
                {}
                (re-seq #"(\w+)\s*=\s*\((\w+),\s*(\w+)\)" input))]

    {:instructions instructions
     :graph graph}))

(defn solve
  [{:keys [graph instructions start finish?]}]
  (loop [current start
         step 0
         commands instructions]
    (if (finish? current)
      step
      (let [commands (or (seq commands)
                         instructions)
            branch (case (first commands)
                     \L :left
                     \R :right)]
        (recur (get-in graph [current branch])
               (inc step)
               (next commands))))))

(defn part1
  [{:keys [graph instructions]}]
  (solve {:graph graph
          :instructions instructions
          :start "AAA"
          :finish? (partial = "ZZZ")}))

(defn part2
  [{:keys [graph instructions]}]
  (let [starts (filter (fn [key]
                         (= (last key) \A))
                       (keys graph))
        finish? (fn [node]
                  (= (last node) \Z))]
    (->> starts
         (map (fn [start]
                (solve {:graph graph
                        :instructions instructions
                        :start start
                        :finish? finish?})))
         (apply u/lcm))))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_08.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [19951 16342438708751]
    )
  )
