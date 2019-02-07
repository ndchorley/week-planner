(ns week-planner.ramblers
  (:require [clojure.string :as str])
  (:import [org.jsoup Jsoup]))

(defn get-events [group-id]
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
