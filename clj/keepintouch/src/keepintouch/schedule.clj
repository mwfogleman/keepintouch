(ns keepintouch.schedule
  (:require [keepintouch [io :refer :all]]
            [clojure.tools.reader.edn :as edn]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn contacted-parse
  [contacted]
  (f/parse contacted-format contacted))

(defn kit-map
  "Takes a Keep in Touch vector and makes a map with the appropriate keys."
  [[interval contacted & names]]
  {:interval interval :contacted contacted :names names})

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

(defn days-since
  [interval contacted]
  (t/in-days (t/interval (day-to-contact interval contacted) (t/now))))

(defn provide-since
  [m]
  (let [interval (:interval m)
        contacted (:contacted m)]
    (when (time-to-contact? interval contacted)
      (assoc-in m [:since] (days-since interval contacted)))))

(defn remove-nils
  [m]
  (into {} (remove (comp nil? val) m)))

(defn logger
  [fl func]
  (->> fl
       kit-in
       (map kit-map)
       (map kit-update)
       (map provide-since)
       (map remove-nils)
       (remove empty?)
       (sort-by :since)
       (map :names)
       #_(map #(into [] %))
       func
       #_(map println)))

(defn backlog
  [fl]
  (logger fl reverse))

(defn shuflog
  [fl]
  (logger fl shuffle))

(defn todays-date
  []
  (f/unparse contacted-format (t/now)))
