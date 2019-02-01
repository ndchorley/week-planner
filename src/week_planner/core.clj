(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]))

(defn plan [request]
  {:status 200
   :body "Week plan will go here"})

(defn -main [& args]
  (jetty/run-jetty plan {:port 9000}))
