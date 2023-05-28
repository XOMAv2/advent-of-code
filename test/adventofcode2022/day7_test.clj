(ns adventofcode2022.day7-test
  (:require [adventofcode2022.day7 :as d7]
            [clojure.test :refer :all]))

(def input
  "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(deftest part1-test
  (testing "input from the example"
    (let [parsed-input (d7/parse-input input)
          result (d7/part1 parsed-input)]
      (is (= 95437 result)))))

(deftest part2-test
  (testing "input from the example"
    (let [parsed-input (d7/parse-input input)
          result (d7/part2 parsed-input)]
      (is (= 24933642 result)))))
