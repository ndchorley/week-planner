(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]
              [week-planner.ramblers :as ramblers]
              [java-time]))

(declare plan make-plan)

(defn -main [& args]
  (jetty/run-jetty
   (partial plan make-plan)
   {:port (Integer/parseInt
           (if (nil? (System/getenv "PORT"))
             "9000"
             (System/getenv "PORT")))}))

(declare to-para)

(defn plan [make-plan request]
  {:status 200
   :body (str
          (hiccup/html5
           [:head
            [:title "Week plan"]]
           [:body
            [:h1 "Week plan"]
            (map
             (fn [date-events]
               (def date (first date-events))
               (def events (last date-events))

               (cons
                [:p [:b (java-time/format (java-time/formatter "EEEE, d MMMM") date)]]
                (map to-para events)))
             (make-plan))]))})

(defn to-para [event]
  [:p
   (java-time/format (java-time/formatter "H:mm") (:date-time event)) " "
   (:title event) " "
   [:a {:href (:url event)} "More info"]])


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
