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

(defn plan [make-plan request]
  (defn to-para [event]
    [:p
     [:b (:title event)] ", "
     (java-time/format (java-time/formatter "d MMMM uuuu H:mm") (:date-time event)) " "
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
  (def events (sort-by
               :date-time
               (concat
                (ramblers/get-events (:met-walkers ramblers/group-ids))
                (ramblers/get-events (:hike-essex ramblers/group-ids)))))

  (def today (java-time/local-date))
  (take-while
   (fn [e]
     (java-time/before?
      (java-time/local-date (:date-time e))
      (java-time/plus today (java-time/days 7))))
   events))
