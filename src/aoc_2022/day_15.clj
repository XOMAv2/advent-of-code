(ns aoc-2022.day-15
  (:require [aoc.util :as u]
            [clojure.string :as string]))

(defn calc-distance
  [[i j] [v h]]
  (+ (abs (- i v))
     (abs (- j h))))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (fn [row]
              (let [[_ j i h v] (re-matches #"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)" row)
                    sensor [(parse-long i) (parse-long j)]
                    beacon [(parse-long v) (parse-long h)]]
                {:sensor sensor
                 :beacon beacon
                 :distance (calc-distance sensor beacon)})))))

(defn touch?
  [r1 r2]
  (let [[from-1 to-1] r1
        [from-2 to-2] r2]
    (and (<= from-1 to-2)
         (<= from-2 to-1))))

(defn merge-touching-ranges
  [rs]
  (reduce (fn [[from-lowest to-greatest] [from-cur to-cur]]
            [(min from-lowest from-cur) (max to-greatest to-cur)])
          (first rs)
          (rest rs)))

(defn merge-ranges*
  [& rs]
  (loop [r (first rs)
         rs (rest rs)
         result #{}]
    (if r
      (let [[touching non-touching]
            (reduce (fn [[touching non-touching] r-cur]
                      (if (touch? r r-cur)
                        [(conj touching r-cur) non-touching]
                        [touching (conj non-touching r-cur)]))
                    [[r] []]
                    rs)]
        (recur (first non-touching)
               (rest non-touching)
               (conj result (merge-touching-ranges touching))))
      result)))

(defn merge-ranges
  [& rs]
  (loop [prev (apply merge-ranges* rs)]
    (let [new (apply merge-ranges* prev)]
      (if (= prev new)
        prev
        (recur new)))))

(defn calc-covering-ranges
  [row parsed-input]
  (let [covering-ranges (for [{:keys [sensor
                                      distance]} parsed-input

                              :let [[i j] sensor
                                    v-delta (abs (- row i))
                                    h-delta (- distance v-delta)]

                              :when (nat-int? h-delta)

                              :let [from (- j h-delta)
                                    to (+ j h-delta 1)]]
                          [from to])]
    (apply merge-ranges (set covering-ranges))))

(defn part1
  [row parsed-input]
  (let [covering-ranges
        (calc-covering-ranges row parsed-input)

        covered-columns
        (reduce (fn [acc [from to]]
                  (+ acc (- to from)))
                0
                covering-ranges)

        beacons
        (reduce (fn [acc {:keys [beacon]}]
                  (conj acc beacon))
                #{}
                parsed-input)

        beacons-in-the-row
        (->> (keep (fn [[i j]]
                     (when (= row i)
                       j))
                   beacons)
             (set)
             (count))]

    (- covered-columns beacons-in-the-row)))

(defn calc-tuning-frequency
  [[i j]]
  (+ (* j 4000000) i))

(defn part2
  [max-index parsed-input]
  (reduce (fn [_ i]
            (let [covering-ranges (calc-covering-ranges i parsed-input)]
              (when (seq covering-ranges)
                (let [left-range [-10 0]
                      right-range [(+ max-index 1) (+ max-index 11)]
                      extended-covering-ranges (apply merge-ranges
                                                      left-range
                                                      right-range
                                                      covering-ranges)]
                  (when (> (count extended-covering-ranges) 1)
                    (let [j (->> (sort-by first extended-covering-ranges)
                                 (first)
                                 (second))
                          uncovered-beacon [i j]]
                      (reduced (calc-tuning-frequency uncovered-beacon))))))))
          nil
          (range (inc max-index))))

(comment
  (let [input (u/slurp-resource "./aoc_2022/day_15.in")
        parsed-input (parse-input input)]
    ((juxt (partial part1 2000000) (partial part2 4000000)) parsed-input) ; => [5809294 10693731308112]
    )
  )
