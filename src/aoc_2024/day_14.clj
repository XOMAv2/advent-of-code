(ns aoc-2024.day-14
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  ([input]
   (parse-input input 101 103))
  ([input rows columns]
   {:rows rows
    :columns columns
    :robots (->> input
                 (re-seq #"p=([\+-]?\d+),([\+-]?\d+) v=([\+-]?\d+),([\+-]?\d+)")
                 (map (fn [[_ i j di dj]]
                        {:position [(parse-long i) (parse-long j)]
                         :velocity [(parse-long di) (parse-long dj)]})))}))

(defn simulate
  [{:keys [rows columns robots]} seconds]
  (map (fn [{[i j] :position
             [di dj] :velocity
             :as robot}]
         (assoc robot :position [(mod (+ i (* di seconds)) rows)
                                 (mod (+ j (* dj seconds)) columns)]))
       robots))

(defn part1
  [{:keys [rows columns] :as parsed-input}]
  (let [robots' (simulate parsed-input 100)
        rows-half (quot rows 2)
        columns-half (quot columns 2)

        filter-robots
        (fn [[i-from i-to] [j-from j-to]]
          (count
           (filter (fn [{[i j] :position}]
                     (and (<= i-from i) (< i i-to)
                          (<= j-from j) (< j j-to)))
                   robots')))

        q1
        (filter-robots [0 rows-half]
                       [0 columns-half])

        q2
        (filter-robots [0 rows-half]
                       [(+ columns-half (mod columns 2)) columns])

        q3
        (filter-robots [(+ rows-half (mod rows 2)) rows]
                       [0 columns-half])

        q4
        (filter-robots [(+ rows-half (mod rows 2)) rows]
                       [(+ columns-half (mod columns 2)) columns])]

    (* q1 q2 q3 q4)))

(defn robots-str
  [{:keys [rows columns robots]}]
  (let [column->robot-rows
        (reduce (fn [acc {[i j] :position}]
                  (update acc j (fnil conj #{}) i))
                {}
                robots)

        lines
        (for [j (range columns)]
          (apply str (reduce (fn [acc i]
                               (assoc acc i \X))
                             (vec (repeat rows \space))
                             (column->robot-rows j))))]

    (string/join "\n" lines)))

(defn easter-egg?
  [robots]
  (boolean
   (reduce (fn [acc {:keys [position]}]
             (if (contains? acc position)
               (reduced nil)
               (conj acc position)))
           #{}
           robots)))

(defn part2
  [parsed-input]
  (loop [seconds 0]
    (if (easter-egg? (simulate parsed-input seconds))
      seconds
      (recur (inc seconds)))))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_14.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [223020000 7338]
    )
  )

#_(let [input (u/slurp-resource "./aoc_2024/day_14.in")
        parsed-input (parse-input input)
        seconds (part2 parsed-input)]
    (->> (simulate parsed-input seconds)
         (assoc parsed-input :robots)
         (robots-str)
         (println)))
