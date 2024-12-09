(ns aoc-2024.day-09
  (:require [aoc.util :as u]
            [better-cond.core :as b]))

(defn parse-input
  [input]
  (:acc (reduce-kv (fn [{:keys [file? id acc]} _ v]
                     (let [length (parse-long (str v))]
                       (if file?
                         {:acc (into acc (vec (repeat length id)))
                          :file? false
                          :id (inc id)}
                         {:acc (into acc (vec (repeat length (- -1 id))))
                          :file? true
                          :id id})))
                   {:file? true
                    :id 0
                    :acc []}
                   (vec input))))

(defn part1
  [blocks]
  (let [{:keys [spaces files]}
        (reduce-kv (fn [{:keys [spaces files]} block id]
                     (if (nat-int? id)
                       {:spaces spaces
                        :files (conj files [block id])}
                       {:spaces (conj spaces block)
                        :files files}))
                   {:spaces []
                    :files ()}
                   blocks)

        compressed-blocks
        (loop [[space-block & spaces] spaces
               [[file-block file-id] & files] files
               blocks blocks]
          (if (< space-block file-block)
            (recur spaces
                   files
                   (assoc blocks
                          space-block file-id
                          file-block nil))
            blocks))

        checksum
        (reduce-kv (fn [acc block id]
                     (cond-> acc
                       (nat-int? id) (+ (* id block))))
                   0
                   compressed-blocks)]

    checksum))

(defn part2
  [blocks]
  (let [{:keys [spaces space->size]}
        (reduce-kv (fn [{:keys [spaces space->size] :as acc} block id]
                     (if (nat-int? id)
                       acc
                       {:space->size (update space->size id (fnil inc 0))
                        :spaces (cond-> spaces
                                  (nil? (space->size id)) (conj [block id]))}))
                   {:spaces []
                    :space->size {}}
                   blocks)

        {:keys [files file->size]}
        (reduce-kv (fn [{:keys [files file->size] :as acc} block id]
                     (if (nat-int? id)
                       {:file->size (update file->size id (fnil inc 0))
                        :files (cond-> files
                                 (nil? (file->size id)) (conj [block id]))}
                       acc))
                   {:files ()
                    :file->size {}}
                   blocks)

        compressed-blocks
        (loop [spaces spaces
               space->size space->size
               [file & files] files
               blocks blocks]
          (b/cond (nil? file)
                  blocks

                  :let [[file-block file-id] file
                        file-size (file->size file-id)
                        idx (some (fn [[idx [space-block space-id]]]
                                    (when (and (< space-block file-block)
                                               (<= file-size (space->size space-id)))
                                      idx))
                                  (map-indexed vector spaces))]

                  idx
                  (let [[space-block space-id] (get spaces idx)]
                    (recur (update spaces idx (fn [[space-block space-id]]
                                                [(+ space-block file-size) space-id]))
                           (update space->size space-id - file-size)
                           files
                           (as-> blocks $
                             (reduce (fn [acc space-block]
                                       (assoc acc space-block file-id))
                                     $
                                     (range space-block (+ space-block file-size)))
                             (reduce (fn [acc file-block]
                                       (assoc acc file-block nil))
                                     $
                                     (range file-block (+ file-block file-size))))))

                  :else
                  (recur spaces space->size files blocks)))

        checksum
        (reduce-kv (fn [acc block id]
                     (cond-> acc
                       (nat-int? id) (+ (* id block))))
                   0
                   compressed-blocks)]

    checksum))

(comment
  (let [input (u/slurp-resource "./aoc_2024/day_09.in")
        parsed-input (parse-input input)]
    ((juxt part1 part2) parsed-input) ; => [6607511583593 6636608781232]
    )
  )
