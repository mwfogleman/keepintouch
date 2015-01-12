(ns keepintouch.schedule
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [keepintouch [io :refer :all]]))

(defn rand-math
  [& args]
  (apply (first (shuffle [- +])) args))

(defn change-interval
  [w i]
  (* i (rand-math 1 w)))

(defn find-since
  [c]
  (filter #(contains? % :since) c))

(defn backlog
  [fl]
  (->> (in fl)
       find-since
       (sort-by :since)
       (out reverse)))

(defn weightlog
  [fl weight]
  {:pre [(< 0.0 weight 1.0)]}
  (->> (in fl)
       (map #(update-in % [:interval] (partial change-interval weight)))
       (map provide-since) ;; we have to do this again, since the interval has changed
       find-since
       (sort-by :since)
       (out reverse)))

(defn randlog
  [fl]
  (->> fl in (out shuffle)))

(defn contact-if
  [m name]
  ;; TODO Capitalize names and check they are the same
  (let [names (:names m)
        match-results (map (partial = name) names)]
    (if (some true? match-results)
      (assoc-in m [:contacted] (t/now))
      m)))

(defn contact
  [file name]
  (let [input-maps (in file)
        processed (map #(contact-if % name) input-maps)
        prepped (map print-prep processed)
        output (str (str/join "\n\n" prepped) "\n")]
    (spitter file output)))
