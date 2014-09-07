(ns keepintouch.schedule
  (:require [keepintouch [io :refer :all]]
            [clojure.tools.reader.edn :as edn]
            [clj-time.core :as t]
            [clj-time.format :as f]))

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

;; add weight scheduler:
;; For each entry in the data file, _keepintouch_ determines a score to sort the entries by:
;;     today - (last contacted + (interval +/- 25%))
;; for comparison, here's backlog: today - (last contacted + interval)
;; where the `+/- 25%` added or subtracted is randomized each time you run _keepintouch_. Entries with the greatest score will be listed to you first and will tend to be those people that you have not contacted in a while and you wish to contact more often. You don't have to use the program on a regular basis.
;; You can use the Weight Scheduler with either of these commands:
;;     $ keepintouch keepintouch.data schedule weight
;;     $ keepintouch keepintouch.data schedule weight 0.25
;; Note that with the second command, you can specify how much of the score is left up to chance (0.0 <= weight <= 1.0).

(defn backlog
  [fl]
  (->> (kit-in fl)
       (map kit-update)
       (map provide-since)
       (map remove-nils)
       (remove empty?) ;; why is this two lines?
       (sort-by :since)
       (kit-out reverse)))

(defn randlog
  [fl]
  (kit-out shuffle (kit-in fl)))

(defn todays-date
  []
  (f/unparse contacted-format (t/now)))
