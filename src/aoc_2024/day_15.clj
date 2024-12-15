(ns aoc-2024.day-15
  (:require [aoc.util :as u]
            [aoc.matrix2 :as m2]
            [better-cond.core :as b]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (let [[warehouse moves] (string/split input #"\n\n")
        matrix (m2/parse warehouse)]
    {:robot (m2/find matrix \@)
     :boxes (set (m2/find-all matrix \O))
     :walls (set (m2/find-all matrix \#))
     :rows (count matrix)
     :columns (count (first matrix))
     :moves (seq (string/replace moves #"\n" ""))}))

(defn step
  [{[i j :as robot] :robot
    [move & moves] :moves
    :keys [walls boxes rows columns]
    :as parsed-input}]
  (if move
    (let [[di dj lookup-seq]
          (case move
            \^ [-1 0 (map vector (range i -1 -1) (repeat j))]
            \> [0 1 (map vector (repeat i) (range j columns))]
            \v [1 0 (map vector (range i rows) (repeat j))]
            \< [0 -1 (map vector (repeat i) (range j -1 -1))])

          vacant?
          (fn [position]
            (when (and (not (contains? walls position))
                       (not (contains? boxes position))
                       (not= robot position))
              position))

          apply-offset
          (fn [vacant-offset]
            (reduce (fn [{:keys [boxes] :as acc} position]
                      (if-let [[i j :as box] (get boxes position)]
                        (-> acc
                            (update :boxes disj box)
                            (update :boxes conj [(+ i di) (+ j dj)]))
                        acc))
                    (assoc parsed-input :robot [(+ i di) (+ j dj)])
                    (reverse (take vacant-offset (rest lookup-seq)))))

          vacant
          (some vacant? lookup-seq)

          wall
          (some walls lookup-seq)

          parsed-input'
          (b/cond (nil? vacant)
                  parsed-input

                  :let [vacant-offset (u/find-index lookup-seq vacant)]

                  (nil? wall)
                  (apply-offset vacant-offset)

                  :let [wall-offset (u/find-index lookup-seq wall)]

                  (< vacant-offset wall-offset)
                  (apply-offset vacant-offset)

                  :else
                  parsed-input)]

      (assoc parsed-input' :moves moves))
    parsed-input))

(defn part1
  [parsed-input]
  (let [parsed-input' (loop [parsed-input parsed-input]
                        (if (seq (:moves parsed-input))
                          (recur (step parsed-input))
                          parsed-input))]
    (reduce (fn [acc [i j]]
              (+ acc (* 100 i) j))
            0
            (:boxes parsed-input'))))

(defn part2
  [parsed-input]
  #_parsed-input)

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_15.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1465152 ...]
    )
  )
