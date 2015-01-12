(ns keepintouch.io
  (:require [clj-time.core :as t]
            [clojure.tools.reader.edn :as edn]
            [clj-time.format :as f]
            [clojure.string :as str]))

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn read-int [s]
  {:post [(number? %)]}
  (edn/read-string s))

(defn day-to-contact
  [interval contacted]
  (t/plus contacted (t/days interval)))

(defn time-to-contact?
  [interval contacted]
  (t/after? (t/now) (day-to-contact interval contacted)))

(defn days-since
  [interval contacted]
  (t/in-days (t/interval (day-to-contact interval contacted) (t/now))))

(defn provide-since
  [m]
  (let [{:keys [interval contacted]} m]
    (if (time-to-contact? interval contacted)
      (assoc-in m [:since] (days-since interval contacted))
      m)))

(defn kit-map
  "Takes a Keep in Touch sequence and makes a map with the appropriate
  keys."
  [[interval contacted & names]]
  (let [i (read-int interval)
        c (f/parse contacted-format contacted)
        m {:interval i :contacted c :names names}]
    (provide-since m)))

(defn in
  "Takes a keepintouch.data file, and produces maps with the
  appropriate keys and values."
  [f]
  (->> f
       slurp
       str/split-lines
       (map str/trim)
       (partition-by empty?)
       (remove #{'("")})
       (map kit-map)))

(defn kit-pretty-print
  [v]
  (dorun (map println v)))

(defn print-prep
  "Takes a KIT data structure, converts the values into strings, and
  returns one string that will print to a KIT data file, i.e. in the
  right order, on multiple lines."
  [{:keys [interval contacted names]}]
  (let [interval-string  (str interval)
        contacted-string (f/unparse contacted-format contacted)
        ;; names is list comprehended; we need to make a flattened list
        unit             (->> names (list interval-string contacted-string) flatten)]
    (str/join "\n" unit)))

(defn out
  "Takes a map and a function, sorts the map by the names, applies the
  function, and prints the result."
  [func m]
  (->> m
       (map :names)
       func
       flatten
       kit-pretty-print))

(defn spitter
  [file contents]
  (spit file contents :append false))
