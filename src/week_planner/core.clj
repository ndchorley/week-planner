(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [week-planner.handler :as handler]
              [week-planner.plan :as plan]))

(defn -main [& args]
  (def port (System/getenv "PORT"))

  (jetty/run-jetty
   (partial handler/plan plan/make-plan)
   {:port
    (Integer/parseInt
     (if (nil? port) "9000" port))}))
