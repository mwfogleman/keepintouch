(ns keepintouch.core-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as t]
            [keepintouch.core :refer :all]
            [keepintouch.schedule :refer :all]
            [keepintouch.io :refer :all]))

(def today (t/date-time 2011 12 11))

(def test-file
"30
2011/12/01
Recently Contacted

180
2011/11/01
Every Six-Months

60
2008/7/14
Really Overdue")

