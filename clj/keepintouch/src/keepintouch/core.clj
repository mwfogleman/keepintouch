(ns keepintouch.core
  (:require [clojure.string :as str]
            [keepintouch [io :refer :all]
             [schedule :refer :all]
             [cli :refer :all]])
  (:gen-class))

(defn contact-handler
  [file more]
  (let [c (count more)
        s (str/join more)
        name (if (= c 1) s (str/join " " s))]
    (contact file name)))

(defn -main
  "Takes a file and optional schedules."
  ([] (println "I was expecting a file path!"))
  ([file] (backlog file))
  ([file schedule] (backlog file))
  ([file arg & more]
   (cond
     (= arg "contact") (contact-handler file more)
     (= arg "schedule")
     (let [f (first more)
           s (second more)]
       (cond (= f "backlog") (backlog file)
             (= f "random") (randlog file)
             (and (= f "weight")
                  (number? s)) ;; somehow this part is not working
             (weightlog file s)
             (= f "weight") (weightlog file 0.25))))))
