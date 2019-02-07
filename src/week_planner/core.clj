(ns week-planner.core (:gen-class)
    (:require [ring.adapter.jetty :as jetty]
              [hiccup.page :as hiccup]
              [clojure.string :as str])
    (:import [org.jsoup Jsoup]))


(defn get-ramblers-events [group-id]
  (def document (.get (Jsoup/connect
                       (str "https://www.ramblers.org.uk/find-a-walk.aspx?layer=walks&tab=walks&group=" group-id))))

  (def elements (.getElementsByClass document "details"))

  (defn to-event [element]
    (def a-element (first (.getElementsByTag element "a")))
    (def subtitles (.getElementsByClass element "subtitle"))

    (def url (.get (.attributes a-element) "href"))
    (def date-time (str
                    (str/replace (.text (first subtitles)) (re-pattern " \\(.*\\)") "")
                    (str/replace (.text (second subtitles)) "Start time" "")))

    (def route (.text a-element))
    (def title (str route ", " (.text (last subtitles))))

    {:url url :title title :date-time date-time})

  (map to-event elements))

(defn plan [request]
  (defn to-para [event]
    [:p
     [:b (:title event)] ", "
     (:date-time event) " "
     [:a {:href (:url event)} "More info"]])

  (def met-walkers-events (get-ramblers-events "IL50"))

  {:status 200
   :body (str
          (hiccup/html5
           [:head
            [:title "Week plan"]]
           [:body
            [:h1 "Week plan"]
            (map to-para met-walkers-events)]))})

(defn -main [& args]
  (jetty/run-jetty plan {:port (Integer/parseInt (System/getenv "PORT"))}))
