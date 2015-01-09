(ns keepintouch.io-test
  (:require [keepintouch.io :refer :all]
            [midje.sweet :refer :all]))

(def read-test-file "test/keepintouch/read-test-file.data")

(def input-maps (in read-test-file))

(fact "We can print a Keep In Touch data structure like the file format"
      (print-prep (first input-maps)) => "30\n2011/12/01\nRecently Contacted")

(facts "about in"
       (fact "it takes a keepintouch.data file, and produces a list"
             (in read-test-file)
             =>
             list?)
       (fact "that list contains maps"
             (in read-test-file)
             =>
             (partial map map?))
       ;; I used shuffle in this next example to prove that the fact is true for any of the maps
       ;; There is probably a more idiomatic way of doing this
       (fact "each map's keys are :interval, :contacted, and :names"
             (keys (first (shuffle (in read-test-file))))
             =>
             (contains '(:interval :contacted :names) :in-any-order :gaps-ok))
       
       (fact "each map's values are still strings"
             (vals (first (shuffle (in read-test-file))))
             =>
             (partial map string?)))
