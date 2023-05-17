(ns adventofcode2022.day7
  (:require [adventofcode2022.util :as u]
            [clojure.string :as string]))

(defn parse-input
  [input]
  (first (reduce (fn [[filesystem path] line]
                   (let [[a b c] (string/split line #" ")]
                     (cond
                       (= line "$ cd /")
                       [filesystem ["/"]]

                       (= line "$ cd ..")
                       [filesystem (pop path)]

                       (and (= a "$") (= b "cd") (string? c))
                       [(update-in filesystem (conj path c) identity) (conj path c)]

                       (= line "$ ls")
                       [filesystem path]

                       (and (= a "dir") (string? b))
                       [(update-in filesystem (conj path b) identity) path]

                       (and (re-matches #"\d+" a) (string? b))
                       [(assoc-in filesystem (conj path b) (parse-long a)) path]

                       :else
                       (throw (Exception.)))))
                 [{"/" nil} ["/"]]
                 (string/split-lines input))))

(defn get-files-table
  [filesystem]
  (reduce-kv (fn [files filename content]
               (let [path [filename]]
                 (cond
                   (int? content)
                   (conj files [:file path content])

                   (empty? content)
                   (conj files [:dir path 0])

                   :else
                   (let [inner-files (get-files-table content)

                         directory-size
                         (reduce (fn [acc [_type inner-path size]]
                                   (cond-> acc
                                     (= (count inner-path) 1) (+ size)))
                                 0
                                 inner-files)

                         inner-files-with-correct-paths
                         (->> inner-files
                              (map (fn [[type inner-path size]]
                                     [type (into path inner-path) size]))
                              (set))

                         files-d1 (into files inner-files-with-correct-paths)]
                     (conj files-d1 [:dir path directory-size])))))
             #{}
             filesystem))

(defn part1
  [parsed-input]
  (reduce (fn [acc [type _ size]]
            (cond-> acc
              (and (= type :dir) (<= size 100000)) (+ size)))
          0
          (get-files-table parsed-input)))

(defn part2
  [parsed-input]
  (let [files-table (get-files-table parsed-input)
        total-space 70000000
        required-space 30000000
        used-space (->> files-table
                        (u/find-first (fn [[type path _size]]
                                        (and (= type :dir) (= path ["/"]))))
                        (u/third))
        free-space (- total-space used-space)
        need-to-be-freed-space (- required-space free-space)]
    (->> files-table
         (sort-by u/third)
         (u/find-first (fn [[type _path size]]
                         (and (= type :dir) (>= size need-to-be-freed-space))))
         (u/third))))

(comment
  (let [input (u/slurp-resource "day7.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [1513699 7991939]
    )
  )
