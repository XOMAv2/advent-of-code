(ns aoc-2024.day-20
  (:require [aoc.matrix2 :as m2]
            [aoc.util :as u]
            [better-cond.core :as b]))

(defn parse-input
  [input]
  (let [matrix (m2/parse input)
        tracks (m2/find-all matrix \.)
        start (m2/find matrix \S)
        end (m2/find matrix \E)]
    {:rows (count matrix)
     :columns (count (first matrix))
     :tracks (into #{start end} tracks)
     :start start
     :end end}))

(defn neighbors
  [[i j]]
  #{[(dec i) j]
    [i (dec j)] [i (inc j)]
    [(inc i) j]})

(defn neighbors-n
  [n position]
  (loop [iteration 0
         seeds #{position}
         visited #{position}

         result
         #{{:picoseconds iteration
            :position position}}]

    (if (< iteration n)
      (let [iteration-next
            (inc iteration)

            seeds-next
            (reduce (fn [acc option]
                      (into acc
                            (filter (complement visited))
                            (neighbors option)))
                    #{}
                    seeds)

            visited-next
            (into visited seeds-next)

            result-next
            (into result
                  (map (fn [position]
                         {:picoseconds iteration-next
                          :position position}))
                  seeds-next)]

        (recur iteration-next
               seeds-next
               visited-next
               result-next))
      result)))

(defn build-path
  [{:keys [start end tracks]}]
  (loop [current start
         visited #{}
         path []]
    (b/cond
      (= current end)
      (conj path current)

      :let [check (fn [position]
                    (when (and (not (contains? visited position))
                               (contains? tracks position))
                      position))]

      :else
      (recur (some check (neighbors current))
             (conj visited current)
             (conj path current)))))

(defn calc-cheat-frequencies
  [n path]
  (let [path-length (count path)
        path-time (dec path-length)

        position->time
        (reduce-kv (fn [acc index position]
                     (assoc acc position {:time-passed index
                                          :time-left (- path-time index)}))
                   {}
                   path)

        cheats
        (reduce (fn [acc [position {:keys [time-passed]}]]
                  (reduce (fn [acc neighbor]
                            (b/cond :let [neighbor-on-path (get position->time (:position neighbor))]

                                    (nil? neighbor-on-path)
                                    acc

                                    :let [new-path-time (+ time-passed
                                                           (:picoseconds neighbor)
                                                           (:time-left neighbor-on-path))]

                                    (< new-path-time path-time)
                                    (assoc acc [position (:position neighbor)] new-path-time)

                                    :else
                                    acc))
                          acc
                          (neighbors-n n position)))
                {}
                position->time)]

    (->> (vals cheats)
         (map (partial - path-time))
         (frequencies))))

(defn count-frequencies
  [freqs]
  (reduce-kv (fn [acc picoseconds frequency]
               (if (> picoseconds 99)
                 (+ acc frequency)
                 acc))
             0
             freqs))

(defn part1
  [parsed-input]
  (->> (build-path parsed-input)
       (calc-cheat-frequencies 2)
       (count-frequencies)))

(defn part2
  [parsed-input]
  (->> (build-path parsed-input)
       (calc-cheat-frequencies 20)
       count-frequencies))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_20.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1452 999556]
    )
  )
