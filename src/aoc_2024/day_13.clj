(ns aoc-2024.day-13
  (:require [aoc.util :as u]))

(defn parse-input
  [input]
  (->> input
       (re-seq #"Button A: X\+(\d+), Y\+(\d+)\s+Button B: X\+(\d+), Y\+(\d+)\s+Prize: X=(\d+), Y=(\d+)")
       (map (fn [[_ ax ay bx by x y]]
              {:button-a [(parse-long ax) (parse-long ay)]
               :button-b [(parse-long bx) (parse-long by)]
               :prize [(parse-long x) (parse-long y)]}))))

; x = adx * a + bdx * b
; y = ady * a + bdy * b

; ady * x = ady * adx * a + ady * bdx * b
; adx * y = adx * ady * a + adx * bdy * b

; ady * x - adx * y = ady * bdx * b - adx * bdy * b
; b * (ady * bdx - adx * bdy) = ady * x - adx * y
; b = (ady * x - adx * y) / (ady * bdx - adx * bdy) 

; a = (x - bdx * b) / adx

(defn solve
  [{[adx ady] :button-a
    [bdx bdy] :button-b
    [x y] :prize}]
  (let [b (/ (- (* ady x) (* adx y))
             (- (* ady bdx) (* adx bdy)))
        a (/ (- x (* bdx b))
             adx)]
    (when (and (pos-int? a) (pos-int? b))
      (+ (* a 3) b))))

(defn part1
  [parsed-input]
  (reduce + 0 (keep solve parsed-input)))

(defn part2
  [parsed-input]
  (->> parsed-input
       (map (fn [m]
              (update m :prize (fn [[x y]]
                                 (let [c 10000000000000]
                                   [(+ x c) (+ y c)])))))
       (keep solve)
       (reduce + 0)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_13.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [37686 77204516023437]
    )
  )
