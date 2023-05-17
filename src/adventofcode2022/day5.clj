(ns adventofcode2022.day5
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-stacks
  [stacks]
  (let [stacks+stack-numbers (string/split-lines stacks)
        stack-numbers+stacks (reverse stacks+stack-numbers)
        [stack-numbers & stacks-d1] stack-numbers+stacks
        entry=>stack-number (u/re-pos #"\d+" stack-numbers)

        get-column
        (fn [matrix index]
          (reduce (fn [acc row]
                    (let [sym (get row index)]
                      (if (= sym \space)
                        (reduced acc)
                        (conj acc sym))))
                  []
                  matrix))]

    (reduce-kv (fn [acc entry stack-number]
                 (assoc acc (parse-long stack-number) (get-column stacks-d1 entry)))
               (sorted-map)
               entry=>stack-number)))

(defn parse-rules
  [rules]
  (->> (string/split-lines rules)
       (map (partial re-seq #"\d+"))
       (map (partial map parse-long))))

(defn parse-input
  [input]
  (let [[stacks rules] (string/split input #"\n\n")]
    [(parse-stacks stacks) (parse-rules rules)]))

(defn part1
  [[stacks rules]]
  (->> (reduce (fn [stacks [amount from to]]
                 (reduce (fn [stacks _]
                           (let [source (get stacks from)
                                 target (get stacks to)]
                             (assoc stacks
                                    from (pop source)
                                    to (conj target (peek source)))))
                         stacks
                         (range amount)))
               stacks
               rules)
       (vals)
       (map last)
       (apply str)))

(defn part2
  [[stacks rules]]
  (->> (reduce (fn [stacks [amount from to]]
                 (let [source (get stacks from)
                       target (get stacks to)]
                   (assoc stacks
                          from (drop-last amount source)
                          to (u/concatv target (take-last amount source)))))
               stacks
               rules)
       (vals)
       (map last)
       (apply str)))

(comment
  (let [input (u/slurp-resource "day5.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => ["BWNCQRMDB" "NHWZCBNBF"]
    )
  )
