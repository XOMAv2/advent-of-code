(ns aoc-2023.day-05
  (:require [aoc.util :as u]))

(defn parse-input
  [input]
  (let [re
        (re-pattern (str "(?s)"
                         "seeds:(.*?)"
                         "seed-to-soil map:(.*?)"
                         "soil-to-fertilizer map:(.*?)"
                         "fertilizer-to-water map:(.*?)"
                         "water-to-light map:(.*?)"
                         "light-to-temperature map:(.*?)"
                         "temperature-to-humidity map:(.*?)"
                         "humidity-to-location map:(.*?)"))

        [_
         seeds
         seed->soil
         soil->fertilizer
         fertilizer->water
         water->light
         light->temperature
         temperature->humidity
         humidity->location]
        (re-matches re input)

        ->longs
        (comp (partial map parse-long) (partial re-seq #"\d+"))

        ->convertion-map
        (fn [input]
          (->> (->longs input)
               (partition 3)
               (map (fn [[destination source delta]]
                      (let [;; source + shift = destination
                            ;; shift = destination - source
                            shift (- destination source)]
                        {:from source
                         :to (dec (+ source delta)) ;; included
                         :transform (partial + shift)})))
               (sort-by :from)))

        ->seed-ranges
        (fn [input]
          (->> (->longs input)
               (partition 2)
               (map (fn [[init delta]]
                      {:from init
                       :to (dec (+ init delta))}))
               (sort-by :from)))]

    {:seeds (set (->longs seeds))
     :seed-ranges (->seed-ranges seeds)
     :convertion-maps (map ->convertion-map [seed->soil
                                             soil->fertilizer
                                             fertilizer->water
                                             water->light
                                             light->temperature
                                             temperature->humidity
                                             humidity->location])}))

(defn convertion-get
  [convertion-map key]
  (let [val (some (fn [{:keys [from to transform]}]
                    (when (<= from key to)
                      (transform (+ from (- key from)))))
                  convertion-map)]
    (or val key)))

(defn part1
  [parsed-input]
  (->> (reduce (fn [ins convertion-map]
                 (map (partial convertion-get convertion-map) ins))
               (:seeds parsed-input)
               (:convertion-maps parsed-input))
       (apply min)))

(defn part2
  [parsed-input]
  (->> (reduce (fn [ins convertion-map]
                 (loop [ins ins
                        mappings convertion-map
                        outs []]
                   (cond
                     (empty? ins)
                     (sort-by :from outs)

                     (empty? mappings)
                     (sort-by :from (into outs ins))

                     :else
                     (let [[{:keys [from to]} & rem-ins] ins
                           [{start :from end :to :keys [transform]} & rem-mappings] mappings]
                       (cond
                         (> from end)
                         (recur ins
                                rem-mappings
                                outs)

                         (< to start)
                         (recur rem-ins
                                mappings
                                (conj outs {:from from
                                            :to to}))

                         (< from start end to)
                         (recur (conj rem-ins {:from (inc end)
                                               :to to})
                                rem-mappings
                                (-> outs
                                    (conj {:from from
                                           :to (dec start)})
                                    (conj {:from (transform start)
                                           :to (transform end)})))

                         (<= start from to end)
                         (recur rem-ins
                                mappings
                                (conj outs {:from (transform from)
                                            :to (transform to)}))

                         (<= start from end to)
                         (recur (conj rem-ins {:from (inc end)
                                               :to to})
                                rem-mappings
                                (conj outs {:from (transform from)
                                            :to (transform end)}))

                         (<= from start to end)
                         (recur rem-ins
                                mappings
                                (-> outs
                                    (conj {:from from
                                           :to (dec start)})
                                    (conj {:from (transform start)
                                           :to (transform to)}))))))))
               (:seed-ranges parsed-input)
               (:convertion-maps parsed-input))
       (first)
       (:from)))

(comment
  (let [input (u/slurp-resource "./aoc_2023/day_05.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [240320250 28580589]
    )
  )
