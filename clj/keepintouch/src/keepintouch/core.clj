(ns keepintouch.core
  (:require [clojure.string :as s]
            [clj-time.core :as t]
            [clj-time.format :as f])
  (:gen-class))

(defn kit-in
  "Takes a keepintouch.data file, and produces a list of lists
  like ("30" "2014/08/30" "Jimmy")
  or ("30" "2014/02/24" "Grandma" "Grandpa")."
  [f]
  (->> f
       slurp
       s/split-lines
       (map s/trim)
       (partition-by empty?)
       (remove #{'("")})))

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn contacted-parse
  [contacted]
  (f/parse contacted-format contacted))

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
