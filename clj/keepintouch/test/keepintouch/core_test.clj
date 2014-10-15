(ns keepintouch.core-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as t]
            [keepintouch.core :refer :all]
            [keepintouch.schedule :refer :all]
            [keepintouch.io :refer :all]))

(def today (t/date-time 2011 12 11))

(def test-file "test/keepintouch/test-file.data")

(facts "about kit-in"
       (fact "it takes a keepintouch.data file, and produces a list"
             (kit-in test-file) => list?)
       (fact "that list contains maps"
             (kit-in test-file) => (partial map map?))
       ;; I used shuffle in this next example to prove that the fact is true for any of the maps
       ;; There is probably a more idiomatic way of doing this
       (fact "each map's keys are :interval, :contacted, and :names"
             (keys (first (shuffle (kit-in test-file)))) => '(:interval :contacted :names))
       (fact "each map's values are still strings"
             (vals (first (shuffle (kit-in test-file)))) => (partial map string?)))
