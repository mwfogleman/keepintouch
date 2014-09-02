(defproject keepintouch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.8.0"]]
  :main ^:skip-aot keepintouch.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
