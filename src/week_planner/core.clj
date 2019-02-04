(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]))

(defn plan [request]
  {:status 200
   :body (str
          (hiccup/html5
           [:head
            [:title "Week plan"]]
           [:body "Week plan will go here"]))})

(defn -main [& args]
  (jetty/run-jetty plan {:port (Integer/parseInt (System/getenv "PORT"))}))

