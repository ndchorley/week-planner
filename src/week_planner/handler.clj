(ns week-planner.handler
  (:require [hiccup.page :as hiccup]
            [java-time]))

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
               (let [date (first date-events)
                     events (last date-events)]
                 (cons
                  [:p [:b (java-time/format (java-time/formatter "EEEE, d MMMM") date)]]
                  (map to-para events))))
             (make-plan))]))})

(defn- to-para [event]
  [:p
   (java-time/format (java-time/formatter "H:mm") (:date-time event)) " "
   (:title event) " "
   [:a {:href (:url event)} "More info"]])
