(defproject csv-import "0.1.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.postgresql/postgresql "9.3-1103-jdbc41"]
                 [clojure-csv/clojure-csv "2.0.1"]]
  :main ^:skip-aot csv-import.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
