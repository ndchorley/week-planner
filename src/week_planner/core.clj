(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]
              [week-planner.ramblers :as ramblers]
              [week-planner.handler :as handler]
              [java-time]))

(declare make-plan)

(defn -main [& args]
  (jetty/run-jetty
   (partial handler/plan make-plan)
   {:port (Integer/parseInt
           (if (nil? (System/getenv "PORT"))
             "9000"
             (System/getenv "PORT")))}))

(declare in-week-ahead?)

(defn make-plan []
  (def events (sort-by :date-time (ramblers/get-events)))
  (def today (java-time/local-date))

  (into
   (sorted-map)
   (group-by
    (fn [e] (java-time/local-date (:date-time e)))
    (take-while in-week-ahead? events))))

(defn in-week-ahead? [event]
  (java-time/before?
   (java-time/local-date (:date-time event))
   (java-time/plus today (java-time/days 8))))
