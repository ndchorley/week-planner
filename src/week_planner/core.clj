(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]
              [clojure.string :as str])
    (:import [org.jsoup Jsoup]))

(defn get-met-walkers-events []
  (def document (.get (Jsoup/connect "https://www.metropolitan-walkers.org.uk/walks/upcoming-walks.html")))

  (defn to-event [element]
    (def field-parts (str/split (.text element) (re-pattern ",")))

    {:date (str/join (take 2 field-parts))
     :title (str/trim (str/join (take-last 2 field-parts)))})

  (map to-event (.getElementsByClass document "walksummary")))

(defn plan [request]
  (defn to-para [event] [:p [:b (:title event)] ", " (:date event)])

  {:status 200
   :body (str
          (hiccup/html5
           [:head
            [:title "Week plan"]]
           [:body
            [:h1 "Week plan"]
            (map to-para (get-met-walkers-events))]))})

(defn -main [& args]
  (jetty/run-jetty plan {:port (Integer/parseInt (System/getenv "PORT"))}))
