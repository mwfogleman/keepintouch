(ns keepintouch.io
  (:require [clojure.string :as s]))

(declare kit-map)

(defn in
  "Takes a keepintouch.data file, and produces maps with the
  appropriate keys and values."
  [f]
  (->> f
       slurp
       s/split-lines
       (map s/trim)
       (partition-by empty?)
       (remove #{'("")})
       (map kit-map)))

(defn kit-map
  "Takes a Keep in Touch sequence and makes a map with the appropriate
  keys."
  [[interval contacted & names]]
  {:interval interval :contacted contacted :names names})

(defn kit-pretty-print
  [v]
  (dorun (map println v)))

(defn print-prep
  "Takes a KIT data structure (with string vals) into one string that
  will print in a way that looks like the KIT data file."
  [{:keys [interval contacted names]}]
  (clojure.string/join "\n" (flatten (list interval contacted names))))

(defn out
  "Takes a map and a function, sorts the map by the names, applies the
  function, and prints the result."
  [func m]
  (->> m
       (map :names)
       func
       flatten
       kit-pretty-print))
