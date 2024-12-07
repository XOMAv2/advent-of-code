(ns aoc-2024.day-07
  (:require [aoc.util :as u]
            [better-cond.core :as b]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (->> (string/split-lines input)
       (map (comp (juxt first rest)
                  (partial map parse-long)
                  (partial re-seq #"\d+")))))

(defn pow
  [a b]
  (reduce (fn [acc _]
            (* acc a))
          1
          (range b)))

(defn ||
  [a b]
  (+ (* a (pow 10 (u/count-digits b))) b))

(defn XX
  [a b]
  (/ (- a b) (pow 10 (u/count-digits b))))

(defn selections-memo
  [items n]
  ((memoize combo/selections) items n))

(defn can-be-solved?
  [result functions args]
  (zero? (reduce (fn [acc [f arg]]
                   (b/cond
                     (zero? acc) (reduced -1)

                     (> arg acc)
                     (reduced -1)

                     :let [x (f acc arg)]
                     
                     (not (zero? (mod x 1)))
                     (reduced -1)
                     
                     :else
                     x))
                 result
                 (map vector functions args))))

(defn solution-fast
  [result functions args]
  (let [args' (reverse args)]
    (some (fn [functions]
            (can-be-solved? result (conj (vec functions) -) args'))
          (selections-memo functions (dec (count args))))))

(defn part1
  [parsed-input]
  (->> parsed-input
       (pmap (fn [[result args]]
                 (when (solution-fast result [- /] args) #_(solution-slow result [- /] args)
                   result)))
       (keep identity)
       (reduce + 0)))

(defn solve
  [functions args]
  (reduce (fn [acc [f x]]
            (f acc x))
          (first args)
          (map vector functions (rest args))))

(defn solution-slow
  [result functions args]
  (some (fn [functions]
          (= result (solve functions args)))
        (selections-memo functions (dec (count args)))))

(defn part2
  [parsed-input]
  (->> parsed-input
       (pmap (fn [[result args]]
               (when (solution-fast result [- / XX] args) #_(solution-slow result [+ * ||] args)
                 result)))
       (keep identity)
       (reduce + 0)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_07.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [5512534574980 328790210468594]
    )
  )
