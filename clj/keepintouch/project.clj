(defproject keepintouch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [org.clojure/tools.reader "0.8.13"]
                 [clj-time "0.9.0"]]
  :main ^:skip-aot keepintouch.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev
             {:dependencies [[midje "1.7.0-SNAPSHOT"]]}})
