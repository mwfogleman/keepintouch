(ns keepintouch.io
  (:require [clojure.string :as s]))

(declare kit-map)

(defn kit-in
  "Takes a keepintouch.data file, and produces a list of vectors
  containing the relevant strings."
  [f]
  (->> f
       slurp
       s/split-lines
       (map s/trim)
       (partition-by empty?)
       (remove #{'("")})
       (map #(into [] %))
       (map kit-map)))

(defn kit-map
  "Takes a Keep in Touch vector and makes a map with the appropriate
  keys."
  [[interval contacted & names]]
  {:interval interval :contacted contacted :names names})

(defn kit-out
  "Takes a map and a function, sorts the map by the names, applies the
  function, and prints the result."
  [func m]
  (->> m
       (map :names)
       func
       flatten
       (into [])
       (map println)))
