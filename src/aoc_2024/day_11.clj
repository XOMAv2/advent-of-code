(ns aoc-2024.day-11
  (:require [aoc.util :as u]
            [clojure.math :as math]
            [better-cond.core :as b]))

(defn parse-input
  [input]
  (map parse-long (re-seq #"\d+" input)))

(defn split-number
  [n digit-count]
  (let [half (bit-shift-right digit-count 1)
        divisor (int (math/pow 10 half))]
    [(quot n divisor) (mod n divisor)]))

(defn apply-rules
  [stone]
  (b/cond (zero? stone)
          [1]
  
          :let [digit-count (u/count-digits stone)]
  
          (even? digit-count)
          (split-number stone digit-count)
  
          :else
          [(* stone 2024)]))

(def apply-rules-memo
  (memoize apply-rules))

(defn make-step-seq
  [[stone & stones]]
  (b/cond :when stone

          :let [[a b] (apply-rules-memo stone)]

          (and a b)
          (lazy-seq (cons a (lazy-seq (cons b (make-step-seq stones)))))

          :else
          (lazy-seq (cons a (make-step-seq stones)))))

(defn iterate-reduce
  [n f val]
  (loop [val val
         n n]
    (if (pos? n)
      (recur (f val) (dec n))
      val)))

(defn part1
  [parsed-input]
  (->> parsed-input
       (iterate-reduce 25 make-step-seq)
       (count)))

(defn make-step-hash-map
  [stones-m]
  (let [add-stones (fn [acc k freq]
                     (u/update! acc k (fnil + 0) freq))]
    (persistent!
     (reduce-kv (fn [acc stone freq]
                  (b/cond :let [[a b] (apply-rules-memo stone)]

                          (and a b)
                          (-> acc
                              (add-stones a freq)
                              (add-stones b freq))

                          :else
                          (add-stones acc a freq)))
                (transient {})
                stones-m))))

(defn part2
  [parsed-input]
  (->> (frequencies parsed-input)
       (iterate-reduce 75 make-step-hash-map)
       (reduce-kv (fn [acc _k v]
                    (+ acc v))
                  0)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_11.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [186424 219838428124832]
    )
  )
