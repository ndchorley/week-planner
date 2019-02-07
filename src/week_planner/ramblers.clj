(ns week-planner.ramblers
  (:require [clojure.string :as str])
  (:import [org.jsoup Jsoup]))

(declare to-event)

(def group-ids {:met-walkers "IL50"})

(defn get-events [group-id]
  (def document (.get (Jsoup/connect
                       (str "https://www.ramblers.org.uk/find-a-walk.aspx?layer=walks&tab=walks&group=" group-id))))

  (def elements (.getElementsByClass document "details"))

  (map to-event elements))

(defn- to-event [element]
  (def a-element (first (.getElementsByTag element "a")))
  (def subtitles (.getElementsByClass element "subtitle"))

  (def date-time (str
                  (str/replace (.text (first subtitles)) (re-pattern " \\(.*\\)") "")
                  (str/replace (.text (second subtitles)) "Start time" "")))

  (def route (.text a-element))

  {:url (.get (.attributes a-element) "href")
   :title (str route ", " (.text (last subtitles)))
   :date-time date-time})
