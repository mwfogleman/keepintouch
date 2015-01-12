(ns keepintouch.io-test
  (:require [keepintouch.io :refer :all]
            [midje.sweet :refer :all]))

(def read-test-file "resources/read-test-file.data")
(def write-test-file "resources/write-test-file.data")

(def write-test-contents (slurp write-test-file))

(def input-maps (in read-test-file))

(fact "We can print a Keep In Touch data structure like the file format"
      (print-prep (first input-maps)) => "30\n2011/12/01\nRecently Contacted")

(facts "about in"
       (let [read-in (in read-test-file)
             {:keys [interval contacted names]} (first read-in)]
         (fact "it takes a keepintouch.data file, and produces a list"
               read-in =>  list?)
         (fact "that list contains maps"
               (->> read-in (map type) set) => #{clojure.lang.PersistentArrayMap})
         (fact "each map contains :interval, :contacted, :names, and (if appropriate) :since"
               (->> read-in (map keys) set first) => (contains '(:interval :contacted :names :since) :gaps-ok :in-any-order))
         (fact "the interval key is a number"
               interval => number?)
         (fact "the contacted key is a Date Object"
               (type contacted) => org.joda.time.DateTime)
         (fact "the names are a list of names"
               names => list?
               (->> names (map type) set) => #{java.lang.String})
         (let [a-since (->> read-in keepintouch.schedule/find-since first)]
           (fact "provide-since means that some of the maps have the key :since"
                 (contains? a-since :since) => true)
           (fact "and its value is a number"
                 (:since a-since)
                 => number?))))

(facts "about print-prep"
       (let [kit-map (->> (in read-test-file) keepintouch.schedule/find-since first)
             prepped (print-prep kit-map)]
         ;; e.g. "30\n2011/12/01\nRecently Contacted"
         (fact "it creates a string"
               prepped => string?)
         (fact "it has two newlines"
               (re-seq #"\n" prepped) => '("\n" "\n"))))
