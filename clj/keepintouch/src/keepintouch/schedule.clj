(ns keepintouch.schedule
  (:require [keepintouch [io :refer :all]]
            [clojure.tools.reader.edn :as edn]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn kit-update
  "Takes an initial Keep in Touch map with strings, turns the interval
  into a number, and the contacted date into an actionable Date
  object."
  [m]
  (-> m
      (update-in [:interval] edn/read-string)
      (update-in [:contacted] (partial f/parse contacted-format))))

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


(defn rand-math
  [& args]
  (apply (first (shuffle [- +])) args))

(defn change-interval
  [w i]
  (* i (rand-math 1 w)))

(defn backlog
  [fl]
  (->> (kit-in fl)
       (map kit-update)
       (map provide-since)
       (map remove-nils)
       (remove empty?) ;; why is this two lines?
       (sort-by :since)
       (kit-out reverse)))

(defn weightlog
  [fl weight]
  (when (and (< weight 1.0) (> weight 0.0))
    (->> (kit-in fl)
         (map kit-update)
         (map #(update-in % [:interval] (partial change-interval weight)))
         (map provide-since)
         (map remove-nils)
         (remove empty?)
         (sort-by :since)
         (kit-out reverse))))

(defn randlog
  [fl]
  (kit-out shuffle (kit-in fl)))

(defn todays-date
  []
  (f/unparse contacted-format (t/now)))
