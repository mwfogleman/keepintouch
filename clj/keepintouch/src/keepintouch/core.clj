(ns keepintouch.core
  (:require [clojure.string :as s]
            [clojure.tools.reader.edn :as edn]
            [clj-time.core :as t]
            [clj-time.format :as f])
  (:gen-class))

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

(defn kit-map
  "Takes a Keep in Touch vector and makes a map with the appropriate keys."
  [[interval contacted & names]]
  {:interval interval :contacted contacted :names names})

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn contacted-parse
  [contacted]
  (f/parse contacted-format contacted))

(defn kit-update
  "Takes an initial Keep in Touch map with strings, turns the interval
  into a number, and the contacted date into an actionable Date
  object."
  [m]
  (-> m
      (update-in [:interval] edn/read-string)
      (update-in [:contacted] contacted-parse)))

(defn day-to-contact
  [interval contacted]
  (t/plus contacted (t/days interval)))

(defn time-to-contact?
  [interval contacted]
  (t/after? (t/now) (day-to-contact interval contacted)))

(defn todays-date
  []
  (f/unparse contacted-format (t/now)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
