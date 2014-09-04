(ns keepintouch.io
  (:require [clojure.string :as s]))

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
       (map #(into [] %))))
