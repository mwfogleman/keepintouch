(defproject keepintouch "0.2.0-SNAPSHOT"
  :description "Keep in Touch with friends and family"
  :url "https://www.github.com/mwfogleman/keepintouch"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.reader "0.10.0"]
                 [clj-time "0.13"]
                 [acyclic/squiggly-clojure "0.1.8"]]
  :main ^:skip-aot keepintouch.core
  :target-path "target/%s"
  :plugins [[lein-environ "1.0.0"]]
  :profiles {:uberjar {:aot :all}
             :dev
             {:dependencies [[midje "1.8.3"]]
              :env {:squiggly {:checkers [:eastwood :kibit]
                               :eastwood-exclude-linters [:unlimited-use]}}}})
