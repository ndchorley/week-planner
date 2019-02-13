(defproject week-planner "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [hiccup "2.0.0-alpha2"]
                 [clojure.java-time "0.3.2"]
                 [hickory "0.7.1"]
                 [clj-http "3.9.1"]]
  :min-lein-version "2.0.0"
  :main week-planner.core
  :aot [week-planner.core]
  :uberjar-name "week-planner-standalone.jar")
