(ns aoc-2024.day-12
  (:require [aoc.matrix2 :as m2]
            [aoc.util :as u]
            [better-cond.core :as b]))

(def parse-input
  m2/parse)

(defn find-region
  ([matrix [i j :as position]]
   (when (get-in matrix position)
     (->> #{position}
          (find-region matrix position [(inc i) j])
          (find-region matrix position [(dec i) j])
          (find-region matrix position [i (inc j)])
          (find-region matrix position [i (dec j)]))))
  ([matrix seed [i j :as position] visited]
   (cond (not= (get-in matrix seed)
               (get-in matrix position))
         visited

         (contains? visited position)
         visited

         :else
         (->> (conj visited position)
              (find-region matrix seed [(inc i) j])
              (find-region matrix seed [(dec i) j])
              (find-region matrix seed [i (inc j)])
              (find-region matrix seed [i (dec j)])))))

(defn find-regions
  [matrix]
  (let [rows (count matrix)
        columns (count (first matrix))]
    (loop [i 0
           j 0
           taken #{}
           regions #{}]
      (b/cond :let [position (if (< i rows)
                               [i j]
                               (let [j' (inc j)]
                                 (when (< j' columns)
                                   [0 j'])))]

              (nil? position)
              regions

              :let [[i j] position]

              (contains? taken position)
              (recur (inc i) j taken regions)

              :else
              (let [region (find-region matrix position)]
                (recur (inc i) j (into taken region) (conj regions region)))))))

(defn perimeter
  [region]
  (reduce (fn [acc [i j]]
            (+ acc
               (if (contains? region [(inc i) j]) 0 1)
               (if (contains? region [(dec i) j]) 0 1)
               (if (contains? region [i (inc j)]) 0 1)
               (if (contains? region [i (dec j)]) 0 1)))
          0
          region))

(defn part1
  [matrix]
  (reduce (fn [acc region]
            (+ acc (* (perimeter region) (count region))))
          0
          (find-regions matrix)))

(defn find-boundaries
  [region]
  {:row->column-boundaries
   (reduce (fn [acc [i j]]
             (-> acc
                 (update-in [i :start] (fnil min j) j)
                 (update-in [i :finish] (fnil max j) j)))
           {}
           region)

   :column->row-boundaries
   (reduce (fn [acc [i j]]
             (-> acc
                 (update-in [j :start] (fnil min i) i)
                 (update-in [j :finish] (fnil max i) i)))
           {}
           region)})

(defn outer-sides
  [boundaries]

  (let [count-sides
        (fn [x-axis->y-axis-boundaries]
          (reduce (fn [acc [[_ a] [_ b]]]
                    (cond-> acc
                      (not= (:start a) (:start b)) (update :start inc)
                      (not= (:finish a) (:finish b)) (update :finish inc)))
                  {:start 1
                   :finish 1}
                  (partition 2 1 (sort-by first x-axis->y-axis-boundaries))))

        vertical-sides
        (count-sides (:row->column-boundaries boundaries))

        horizontal-sides
        (count-sides (:column->row-boundaries boundaries))]

    (+ (:start vertical-sides)
       (:finish vertical-sides)
       (:start horizontal-sides)
       (:finish horizontal-sides))))

(outer-sides #{[0 0] [1 0] [1 1] [2 1] [3 1] [4 1] [4 2]})

(outer-sides #{[3 0] [3 1] [3 2]})

(defn part2
  [matrix]

  (->> (find-regions matrix)
       (map (fn [region]
              (let [boundaries (find-boundaries region)]
                {:region region
                 :outer-sides (outer-sides boundaries)

                 :surrounding-plots
                 (as-> #{} $
                   (reduce-kv (fn [acc row {:keys [start finish]}]
                                (conj acc
                                      [row (dec start)]
                                      [row (inc finish)]))
                              $
                              (:row->column-boundaries boundaries))
                   (reduce-kv (fn [acc column {:keys [start finish]}]
                                (conj acc
                                      [(dec start) column]
                                      [(inc finish) column]))
                              $
                              (:column->row-boundaries boundaries)))})))
       (reduce (fn [acc {:keys [region outer-sides]}]
                 (+ acc (* outer-sides (count region))))
               0)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_12.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1424006 ...]
    )
  )


(part2 (parse-input "OOOOO
OXOXO
OOOOO
OXOXO
OOOOO"))

; 436 = 1 * 4 + 1 * 4 + 1 * 4 + 1 * 4 + 21 * 20 = 16 + 420 = 436
