(ns keepintouch.schedule-test
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [keepintouch.io :refer :all]
            [keepintouch.schedule :refer :all]
            [midje.sweet :refer :all]))

(def read-test-file "resources/read-test-file.data")
(def write-test-file "resources/write-test-file.data")

(def write-test-contents (slurp write-test-file))

(contact write-test-file "Really-Overdue")
(contact write-test-file "Recently Contacted")
(def new-write-test-contents (slurp write-test-file))

(facts "about contact"
       (fact "it changes the file"
             (not= write-test-contents new-write-test-contents) => true)
       (fact "it can say you contacted someone"
             (->> write-test-file
                  in
                  (map :contacted)
                  (map (partial f/unparse contacted-format)))
             =>
             (contains
              (f/unparse contacted-format (t/now)))))

(spitter write-test-file write-test-contents)

(facts "about backlog"
       (fact "it takes a file and it returns nil"
             (backlog read-test-file) => nil?)
       ;; but its side effect is printing a string of names in order
       )
