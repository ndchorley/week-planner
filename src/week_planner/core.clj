(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [week-planner.handler :as handler]))

(defn -main [& args]
  (jetty/run-jetty
   handler/plan
   {:port
    (let [port (System/getenv "PORT")]
         (Integer/parseInt
          (if (nil? port) "9000" port)))}))
