(ns keepintouch.schedule-test
  (:require [keepintouch.schedule :refer :all]
            [keepintouch.io :refer :all]
            [midje.sweet :refer :all]))

(def read-test-file "test/keepintouch/read-test-file.data")
(def write-test-file "test/keepintouch/write-test-file.data")

(def write-test-contents (slurp write-test-file))

(facts "about kit-update"
       (fact "it takes the product of in, and returns a new list"
             (map kit-update (in read-test-file))
             =>
             list?)
       (fact "that list still contains maps"
             (map kit-update (in read-test-file))
             =>
             (partial map map?))
       ;; The aforementioned (ab)use of shuffle is present here, too
       (fact "and each map's keys are still :interval, :contacted, and :names"
             (keys (first (shuffle (map kit-update (in read-test-file)))))
             =>
             (contains '(:interval :contacted :names) :in-any-order :gaps-ok))
       (fact "but the :interval values are now numbers"
             (:interval (first (shuffle (map kit-update (in read-test-file)))))
             =>
             number?)
       (fact "and the :contacted values are now date objects"
             (type (:contacted (first (shuffle (map kit-update (in read-test-file))))))
             =>
             org.joda.time.DateTime))

(facts "about provide-since"
       (fact "it takes the product of kit-update, and returns a new list"
             (map provide-since (map kit-update (in read-test-file)))
             =>
             list?)
       (fact "that list still contains maps"
             (map provide-since (map kit-update (in read-test-file)))
             =>
             (partial map map?))
       ;; Shuffle ahoy!
       (fact "but there is a new key, :since"
             (keys (first (shuffle (map provide-since (map kit-update (in read-test-file))))))
             =>
             (contains '(:since :interval :contacted :names) :in-any-order :gaps-ok))
       (fact "and the value of that new key is a number"
             (:since (first (shuffle (map provide-since (map kit-update (in read-test-file))))))
             =>
             number?))

(facts "about backlog"
       (fact "it takes a file and it returns nil"
             (backlog read-test-file) => nil?)
       ;; but its side effect is printing a string of names in order
       )

(contact write-test-file "Really-Overdue")
(contact write-test-file "Recently Contacted")
(def new-write-test-contents (slurp write-test-file))

(facts "about contact"
       (fact "it changes the file"
             (not= write-test-contents new-write-test-contents) => true)
       (fact "it can say you contacted someone"
             (set (map :contacted (in write-test-file))) => (contains #{(todays-date)})))

(spitter write-test-file write-test-contents)