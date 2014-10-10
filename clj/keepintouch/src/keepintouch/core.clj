(ns keepintouch.core
  (:require [keepintouch [io :refer :all]
             [schedule :refer :all]
             [cli :refer :all]])
  (:gen-class))

(defn -main
  "Takes a file and optional schedules."
  ([] (println "I was expecting a file path!"))
  ([file] (backlog file))
  ([file schedule] (backlog file))
  ([file schedule & more]
     (let [f (first more)
           s (second more)]
       (cond (= f "backlog") (backlog file)
             (= f "random") (randlog file)
             (and (= f "weight")
                  (number? s)) ;; somehow this part is not working
             (weightlog file s)
             (= f "weight") (weightlog file 0.25)))))
