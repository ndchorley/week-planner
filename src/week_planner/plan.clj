(ns week-planner.plan
  (:require [java-time]
            [week-planner.ramblers :as ramblers]))

(declare in-week-ahead?)

(defn make-plan []
  (let [events (sort-by :date-time (ramblers/get-events))]
    (into
     (sorted-map)
     (group-by
      (fn [e] (java-time/local-date (:date-time e)))
      (take-while in-week-ahead? events)))))

(defn- in-week-ahead? [event]
  (let [today (java-time/local-date)]
    (java-time/before?
     (java-time/local-date (:date-time event))
     (java-time/plus today (java-time/days 8)))))
