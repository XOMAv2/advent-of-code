(ns adventofcode2022.day4
  (:require [adventofcode2022.util :as u]
            [clojure.set :as cset]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map #(string/split % #"[-,]"))
       (map #(map parse-long %))))

(comment
  ;; Better solution idea

  (defn full-overlap?
    [[a b x y]]
    (<= (* (- x a)
           (- y b))
        0))

  (defn overlap?
    [[a b x y]]
    (<= (* (- x b)
           (- y a))
        0))
  )

(defn part1
  [parsed-input]
  (reduce (fn [acc [a b x y]]
            (let [set1 (set (range a (inc b)))
                  set2 (set (range x (inc y)))]
              (cond-> acc
                (or (cset/superset? set1 set2)
                    (cset/superset? set2 set1))
                inc)))
          0
          parsed-input))

(defn part2
  [parsed-input]
  (reduce (fn [acc [a b x y]]
            (let [set1 (set (range a (inc b)))
                  set2 (set (range x (inc y)))]
              (cond-> acc
                (or (seq (cset/intersection set1 set2))
                    (seq (cset/intersection set2 set1)))
                inc)))
          0
          parsed-input))

(comment
  (let [input (u/slurp-resource "day4.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [490 921]
    )
  )
