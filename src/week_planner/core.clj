(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [week-planner.handler :as handler]
              [week-planner.plan :as plan]))

(defn -main [& args]
  (jetty/run-jetty
   (partial handler/plan plan/make-plan)
   {:port (Integer/parseInt
           (if (nil? (System/getenv "PORT"))
             "9000"
             (System/getenv "PORT")))}))
