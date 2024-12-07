(ns aoc-2024.day-06
  (:require [aoc.matrix2 :as m2]
            [aoc.util :as u]
            [better-cond.core :as b]))

(defn parse-input
  [input]
  (let [matrix (m2/parse input)
        obstructions (m2/find-all matrix \#)]
    {:size (m2/size matrix)
     :position (m2/find matrix \^)
     :direction :up
     :obstructions (set obstructions)

     :row->obstructions
     (reduce (fn [acc [i j]]
               (update acc i (fnil conj (sorted-set)) j))
             {}
             obstructions)

     :column->obstructions
     (reduce (fn [acc [i j]]
               (update acc j (fnil conj (sorted-set)) i))
             {}
             obstructions)}))

(defn traverse
  [{[rows columns] :size
    :keys [direction
           position
           row->obstructions
           column->obstructions]}]
  (let [conj' (fnil conj #{})]

    (loop [direction direction
           [i j] position
           visited (transient {})]

      (case direction
        :up
        (b/cond
          :let [i' (->> (column->obstructions j)
                        (take-while #(< % i))
                        (last))]

          (nil? i')
          {:visited (persistent! (u/update! visited direction update j conj' [i -1]))
           :loop? false}

          :let [range [i i']]

          (get-in visited [direction j range])
          {:visited (persistent! visited)
           :loop? true}

          :else
          (recur :right [(inc i') j] (u/update! visited direction update j conj' range)))

        :right
        (b/cond
          :let [j' (->> (row->obstructions i)
                        (drop-while #(<= % j))
                        (first))]

          (nil? j')
          {:visited (persistent! (u/update! visited direction update i conj' [j columns]))
           :loop? false}

          :let [range [j j']]

          (get-in visited [direction i range])
          {:visited (persistent! visited)
           :loop? true}

          :else
          (recur :down [i (dec j')] (u/update! visited direction update i conj' range)))

        :down
        (b/cond
          :let [i' (->> (column->obstructions j)
                        (drop-while #(<= % i))
                        (first))]

          (nil? i')
          {:visited (persistent! (u/update! visited direction update j conj' [i rows]))
           :loop? false}

          :let [range [i i']]

          (get-in visited [direction j range])
          {:visited (persistent! visited)
           :loop? true}

          :else
          (recur :left [(dec i') j] (u/update! visited direction update j conj' range)))

        :left
        (b/cond
          :let [j' (->> (row->obstructions i)
                        (take-while #(< % j))
                        (last))]

          (nil? j')
          {:visited (persistent! (u/update! visited direction update i conj' [j -1]))
           :loop? false}

          :let [range [j j']]

          (get-in visited [direction i range])
          {:visited (persistent! visited)
           :loop? true}

          :else
          (recur :up [i (inc j')] (u/update! visited direction update i conj' range)))))))

(defn visited->positions
  [visited]
  (let [->positions
        (fn [key step ->position]
          (for [[x ranges] (key visited)
                [from to] ranges
                y (range from to step)]
            (->position x y)))]

    (-> #{}
        (into (->positions :up -1 #(vector %2 %1)))
        (into (->positions :right 1 vector))
        (into (->positions :down 1 #(vector %2 %1)))
        (into (->positions :left -1 vector)))))

(defn part1
  [parsed-input]
  (-> parsed-input traverse :visited visited->positions count))

(defn part2
  [parsed-input]
  (->> (for [position (-> parsed-input traverse :visited visited->positions)
             :when (not= (:position parsed-input) position)]
         position)
       (pmap (fn [[i j :as position]]
               (traverse (-> parsed-input
                             (update :obstructions conj position)
                             (update-in [:row->obstructions i] (fnil conj (sorted-set)) j)
                             (update-in [:column->obstructions j] (fnil conj (sorted-set)) i)))))
       (filter :loop?)
       (count)))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_06.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [5318 1831]
    )
  )
