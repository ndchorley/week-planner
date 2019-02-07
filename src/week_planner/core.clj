(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]
              [week-planner.ramblers :as ramblers]))

(declare plan make-plan)

(defn -main [& args]
  (jetty/run-jetty
   (partial plan make-plan)
   {:port (Integer/parseInt (System/getenv "PORT"))}))

(defn plan [make-plan request]
  (defn to-para [event]
    [:p
     [:b (:title event)] ", "
     (:date-time event) " "
     [:a {:href (:url event)} "More info"]])

  {:status 200
   :body (str
          (hiccup/html5
           [:head
            [:title "Week plan"]]
           [:body
            [:h1 "Week plan"]
            (map to-para (make-plan))]))})

(defn make-plan []
  (ramblers/get-events (:met-walkers ramblers/group-ids)))
