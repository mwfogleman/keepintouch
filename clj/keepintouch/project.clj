(defproject keepintouch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.reader "0.10.0"]
                 [clj-time "0.12.2"]
                 [acyclic/squiggly-clojure "0.1.6"]]
  :main ^:skip-aot keepintouch.core
  :target-path "target/%s"
  :plugins [[lein-environ "1.0.0"]]
  :profiles {:uberjar {:aot :all}
             :dev
             {:dependencies [[midje "1.8.3"]]
              :env {:squiggly {:checkers [:eastwood :kibit]
                               :eastwood-exclude-linters [:unlimited-use]}}}})
