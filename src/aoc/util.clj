(ns aoc.util
  (:require [better-cond.core :as b]
            [clojure.java.io :as io]))

(set! *warn-on-reflection* true)

(defn sum
  [coll]
  (apply + coll))

(defn slurp-resource
  [filename]
  (slurp (io/resource filename)))

(defn char-range
  [start end]
  (->> (range (int start) (inc (int end)))
       (map char)))

(defn concatv
  "Returns a vector representing the concatenation of the elements in the supplied collections `colls`.
   Ex.: (concatv [1 2] [3 4] [5 6 7]) => [1 2 3 4 5 6 7]"
  [& colls]
  (reduce into [] colls))

(defn keepv
  [f coll]
  (reduce (fn [acc a]
            (if-some [b (f a)]
              (conj acc b)
              acc))
          []
          coll))

(defn swap [v i j]
  (assoc v
         j (v i)
         i (v j)))

(defn split-in-half
  [coll]
  (split-at (quot (count coll) 2) coll))

(defn re-pos
  [^java.util.regex.Pattern re s]
  (let [matcher (re-matcher re s)]
    (loop [acc {}]
      (if (.find matcher)
        (recur (assoc acc (.start matcher) (.group matcher)))
        acc))))

(defn re-seq-pos
  [^java.util.regex.Pattern re s]
  (let [matcher (re-matcher re s)]
    (loop [result nil
           find? (.find matcher)]
      (if-not find?
        (when result
          (reverse result))
        (recur (conj result {:start (.start matcher)
                             :end (.end matcher)
                             :group (.group matcher)})
               (.find matcher))))))

(defn find-first
  [f coll]
  (reduce (fn [_ el]
            (when (f el)
              (reduced el)))
          nil
          coll))

(defn third
  [x]
  (first (next (next x))))

(defn reversev
  [coll]
  (vec (reverse coll)))

(defn restv
  [coll]
  (vec (rest coll)))

(defn gcd
  "Greatest common divisor"
  [a b]
  (if (zero? b)
    a
    (recur b (mod a b))))

(defn lcm
  "Least common multiple"
  ([a b]
   (/ (* a b) (gcd a b)))
  ([a b & more]
   (reduce lcm (lcm a b) more)))

(defn find-in-matrix
  [matrix value]
  (reduce-kv (fn [_ i row]
               (when-let [j (reduce-kv (fn [_ j el]
                                         (when (= el value)
                                           (reduced j)))
                                       nil
                                       row)]
                 (reduced [i j])))
             nil
             matrix))

(defn queue
  [& items]
  (reduce conj (clojure.lang.PersistentQueue/EMPTY) items))

(defn bfs
  "Breadth-first search"
  [graph expand-fn start finish?]
  (loop [visited #{}
         to-visit (queue [start])]
    (b/cond
      (empty? to-visit)
      nil

      :let [[path to-visit-d1] ((juxt peek pop) to-visit)
            node (peek path)]

      (contains? visited node)
      (recur visited to-visit-d1)

      (finish? node)
      path

      :else
      (let [visited-d1 (conj visited node)
            add-path (partial conj path)
            children (map add-path (expand-fn graph node))
            to-visit-d2 (into to-visit-d1 children)]
        (recur visited-d1 to-visit-d2)))))

(def absent?
  (complement contains?))
