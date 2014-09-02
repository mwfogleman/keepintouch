(ns keepintouch.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f])
  (:gen-class))

(def contacted-format (f/formatter "yyyy/MM/dd"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
