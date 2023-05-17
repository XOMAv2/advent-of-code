(ns adventofcode2022.day11
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-monkey
  [input]
  (let [lines (string/split-lines input)

        line->long
        (fn [line]
          (parse-long (re-find #"\d+" line)))

        monkey-index (line->long (get lines 0))

        items (->> (get lines 1)
                   (re-seq #"\d+")
                   (mapv parse-long))

        [left operation right] (-> (re-find #" = (.*)" (get lines 2))
                                   (second)
                                   (string/split #" "))
        parsed-left (parse-long left)
        parsed-right (parse-long right)

        operation-fn
        (fn [x]
          (case operation
            "*" (* (or parsed-left x) (or parsed-right x))
            "+" (+ (or parsed-left x) (or parsed-right x))))

        divisor (line->long (get lines 3))
        success-monkey-index (line->long (get lines 4))
        failure-monkey-index (line->long (get lines 5))

        test-fn
        (fn [x]
          (if (zero? (mod x divisor))
            success-monkey-index
            failure-monkey-index))]

    {:index monkey-index
     :items items

     :parsed-left parsed-left
     :parsed-right parsed-right
     :operation operation
     :operation-fn operation-fn

     :divisor divisor
     :success-monkey-index success-monkey-index
     :failure-monkey-index failure-monkey-index
     :test-fn test-fn

     :inspections 0}))

(defn parse-input
  [input]
  (->> (string/split input #"\n\n")
       (map parse-monkey)
       (sort-by :index)
       (vec)))

(defn proceed-turn
  [monkeys decrease-fn monkey]
  (let [{:keys [index operation-fn test-fn items]} monkey]
    (reduce (fn [monkeys item]
              (let [worry-level (operation-fn item)
                    decreased-worry-level (decrease-fn worry-level)
                    send-to-monkey-index (test-fn decreased-worry-level)]
                (assert (not= send-to-monkey-index index))
                (assert (= send-to-monkey-index
                           (:index (get monkeys send-to-monkey-index))))
                (-> monkeys
                    (update-in [send-to-monkey-index :items] conj decreased-worry-level)
                    (update-in [index :items] u/restv)
                    (update-in [index :inspections] inc))))
            monkeys
            items)))

(defn proceed-round
  ([monkeys decrease-fn]
   (proceed-round monkeys decrease-fn 0))
  ([monkeys decrease-fn monkey-index]
   (if (contains? monkeys monkey-index)
     (let [monkey (get monkeys monkey-index)]
       (assert (= monkey-index (:index monkey)))
       (-> (proceed-turn monkeys decrease-fn monkey)
           (recur decrease-fn (inc monkey-index))))
     monkeys)))

(defn calc-monkey-business
  [monkeys]
  (->> (map :inspections monkeys)
       (sort >)
       (take 2)
       (apply *)))

(defn part1
  [monkeys]
  (let [decrease-fn (fn [worry-level]
                      (quot worry-level 3))]
    (calc-monkey-business
     (reduce (fn [monkeys _round-index]
               (proceed-round monkeys decrease-fn))
             monkeys
             (range 20)))))

(defn part2
  [monkeys]
  (let [lcd (apply u/lcm (map :divisor monkeys))
        decrease-fn (fn [worry-level]
                      (mod worry-level lcd))]
    (calc-monkey-business
     (reduce (fn [monkeys _round-index]
               (proceed-round monkeys decrease-fn))
             monkeys
             (range 10000)))))

(comment
  (let [input (u/slurp-resource "day11.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [58056 15048718170]
    )
  )
