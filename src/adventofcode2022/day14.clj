(ns adventofcode2022.day14
  (:require [adventofcode2022.util :as u]
            [better-cond.core :as b]
            [clojure.string :as string]))

(def rock
  \#)

(def air
  \.)

(def source
  \+)

(def sand
  \o)

(def current-sand
  \X)

(defn calc-rock-points
  [paths]
  (set
   (for [path paths
         :let [connection-points (partition 2 1 path)]
         [a-point b-point] connection-points
         :let [[ai aj] a-point
               [bi bj] b-point]
         i (range (min ai bi) (inc (max ai bi)))
         j (range (min aj bj) (inc (max aj bj)))]
     [i j])))

(defn init-field
  [paths]
  (let [source-point [0 500]

        rock-points
        (calc-rock-points paths)

        rock-points+source-point
        (conj rock-points source-point)

        find-min-max
        (juxt (partial apply min) (partial apply max))

        [min-row-index max-row-index]
        (find-min-max (map first rock-points+source-point))

        [min-col-index max-col-index]
        (find-min-max (map second rock-points+source-point))]

    {:source-point source-point
     :current-sand-point source-point
     :rock-points rock-points
     :sand-points #{}
     :row-range [min-row-index (inc max-row-index)]
     :col-range [min-col-index (inc max-col-index)]}))

(defn get-string-field
  [{:keys [source-point
           current-sand-point
           rock-points
           sand-points
           row-range
           col-range]}]

  (let [point->char
        (fn [point]
          (cond
            (= source-point point) source
            (= current-sand-point point) current-sand
            (contains? sand-points point) sand
            (contains? rock-points point) rock
            :else air))

        string-rows
        (for [row (apply range row-range)
              :let [char-row (for [col (apply range col-range)]
                               (point->char [row col]))]]
          (apply str char-row))]

    (string/join \newline string-rows)))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (fn [row]
              (let [unparsed-pairs
                    (string/split row #" -> ")

                    parse-pair
                    (fn [unparsed-pair]
                      (let [[h v] (string/split unparsed-pair #",")]
                        [(parse-long v) (parse-long h)]))]

                (map parse-pair unparsed-pairs))))))

(defn get-possible-placement
  [{:keys [current-sand-point
           rock-points
           sand-points]}]
  (let [[i j] current-sand-point
        not-blocked? (fn [point]
                       (and (u/absent? rock-points point)
                            (u/absent? sand-points point)))]
    (b/cond
      :let [point-1 [(inc i) j]]
      (not-blocked? point-1) point-1

      :let [point-2 [(inc i) (dec j)]]
      (not-blocked? point-2) point-2

      :let [point-3 [(inc i) (inc j)]]
      (not-blocked? point-3) point-3

      :else nil)))

(defn next-step
  [{:keys [source-point
           current-sand-point]
    :as field}]
  (if-let [next-sand-point (get-possible-placement field)]
    (assoc field :current-sand-point next-sand-point)
    (-> field
        (assoc :current-sand-point source-point)
        (update :sand-points conj current-sand-point))))

(defn part1
  [paths]
  (let [field (init-field paths)

        [min-i max-i] (:row-range field)
        max-i' (dec max-i)

        [min-j max-j] (:col-range field)
        max-j' (dec max-j)

        within-initial-range?
        (fn [{[i j] :current-sand-point}]
          (and (<= min-i i max-i')
               (<= min-j j max-j')))]

    (->> (iterate next-step field)
         (take-while within-initial-range?)
         (last)
         (:sand-points)
         (count))))

(defn part2
  [paths]
  (let [floor-row (->> (reduce into #{} paths)
                       (map first)
                       (apply max)
                       (+ 2))

        floor-path (list [floor-row -10000]
                         [floor-row 10000])

        field (init-field (conj paths floor-path))

        ;; Shrink the width of the floor in order to print the field.
        #_#__ (println (get-string-field field))

        source-is-not-reached?
        (fn [{:keys [source-point
                     sand-points]}]
          (u/absent? sand-points source-point))]

    (->> (iterate next-step field)
         (take-while source-is-not-reached?)
         (last)
         (:sand-points)
         (count)
         (inc))))

(comment
  (let [input (u/slurp-resource "day14.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [779 27426]
    )
  )
