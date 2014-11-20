(ns keepintouch.core-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as t]
            [keepintouch.core :refer :all]
            [keepintouch.schedule :refer :all]
            [keepintouch.io :refer :all]))

(def today (t/date-time 2011 12 11))

(def read-test-file "test/keepintouch/read-test-file.data")
(def write-test-file "test/keepintouch/write-test-file.data")

(facts "about kit-in"
       (fact "it takes a keepintouch.data file, and produces a list"
             (kit-in read-test-file)
             =>
             list?)
       (fact "that list contains maps"
             (kit-in read-test-file)
             =>
             (partial map map?))
       ;; I used shuffle in this next example to prove that the fact is true for any of the maps
       ;; There is probably a more idiomatic way of doing this
       (fact "each map's keys are :interval, :contacted, and :names"
             (keys (first (shuffle (kit-in read-test-file))))
             =>
             (contains '(:interval :contacted :names) :in-any-order :gaps-ok))
       
       (fact "each map's values are still strings"
             (vals (first (shuffle (kit-in read-test-file))))
             =>
             (partial map string?)))

(facts "about kit-update"
       (fact "it takes the product of kit-in, and returns a new list"
             (map kit-update (kit-in read-test-file))
             =>
             list?)
       (fact "that list still contains maps"
             (map kit-update (kit-in read-test-file))
             =>
             (partial map map?))
       ;; The aforementioned (ab)use of shuffle is present here, too
       (fact "and each map's keys are still :interval, :contacted, and :names"
             (keys (first (shuffle (map kit-update (kit-in read-test-file)))))
             =>
             (contains '(:interval :contacted :names) :in-any-order :gaps-ok))
       (fact "but the :interval values are now numbers"
             (:interval (first (shuffle (map kit-update (kit-in read-test-file)))))
             =>
             number?)
       (fact "and the :contacted values are now date objects"
             (type (:contacted (first (shuffle (map kit-update (kit-in read-test-file))))))
             =>
             org.joda.time.DateTime))

(facts "about provide-since"
       (fact "it takes the product of kit-update, and returns a new list"
             (map provide-since (map kit-update (kit-in read-test-file)))
             =>
             list?)
       (fact "that list still contains maps"
             (map provide-since (map kit-update (kit-in read-test-file)))
             =>
             (partial map map?))
       ;; Shuffle ahoy!
       (fact "but there is a new key, :since"
             (keys (first (shuffle (map provide-since (map kit-update (kit-in read-test-file))))))
             =>
             (contains '(:since :interval :contacted :names) :in-any-order :gaps-ok))
       (fact "and the value of that new key is a number"
             (:since (first (shuffle (map provide-since (map kit-update (kit-in read-test-file))))))
             =>
             number?))

(facts "about backlog"
       (fact "it takes a file and it returns nil"
             (backlog read-test-file) => nil?)
       ;; but its side effect is printing a string of names in order
       )


(future-facts "about contacted"
              (future-fact "it can say you contacted someone"                           
                           (contact write-test-file "Recently Contacted" date)))
