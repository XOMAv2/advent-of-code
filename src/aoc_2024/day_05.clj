(ns aoc-2024.day-05
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  {:ordering-rules
   (reduce (fn [acc [_ prev next]]
             (update acc
                     (parse-long prev)
                     (fnil conj #{})
                     (parse-long next)))
           {}
           (re-seq #"(\d+)\|(\d+)" input))

   :updates
   (->> (string/split-lines input)
        (mapcat (partial re-seq #"^\d+(?:,\d+)*$"))
        (map (fn [line]
               (->> (string/split line #",")
                    (u/keepv parse-long)))))})

(defn find-error
  [ordering-rules pages]
  (let [page-count (count pages)]
    (first (for [prev (range (dec page-count))
                 next (range (inc prev) page-count)
                 :when (get-in ordering-rules [(get pages next) (get pages prev)])]
             [prev next]))))

(defn group-updates
  [{:keys [ordering-rules updates]}]
  (loop [valid ()
         invalid ()
         [pages & pages-seq] updates]
    (cond (nil? pages)
          {:valid valid
           :invalid invalid}

          (find-error ordering-rules pages)
          (recur valid (conj invalid pages) pages-seq)

          :else
          (recur (conj valid pages) invalid pages-seq))))

(defn calc-middle-index
  [seq]
  (int (/ (dec (count seq)) 2)))

(def calc-sum
  (partial reduce
           (fn [acc pages]
             (+ acc (get pages (calc-middle-index pages))))
           0))

(defn part1
  [parsed-input]
  (->> (group-updates parsed-input)
       (:valid)
       (calc-sum)))

(defn fix
  [ordering-rules pages]
  (if-let [error (find-error ordering-rules pages)]
    (recur ordering-rules (apply u/swap pages error))
    pages))

(defn part2
  [{:keys [ordering-rules] :as parsed-input}]
  (->> (group-updates parsed-input)
       (:invalid)
       (map (partial fix ordering-rules))
       (calc-sum)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_05.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [5747 5502]
    )
  )
