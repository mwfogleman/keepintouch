(ns keepintouch.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f])
  (:gen-class))

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
