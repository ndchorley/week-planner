(ns week-planner.ramblers
  (:require [clojure.string :as str]
            [java-time])
  (:import [org.jsoup Jsoup]))

(declare to-event)

(defn get-events []
  (flatten
   (map
    (fn [gid]
      (let [document (.get (Jsoup/connect
                            (str "https://www.ramblers.org.uk/find-a-walk.aspx?layer=walks&tab=walks&group=" gid)))
            elements (.getElementsByClass document "details")]
        (map to-event elements)))
    #{"IL50" "ES50"})))

(defn- to-event [element]
  (let [a-element (first (.getElementsByTag element "a"))
        subtitles (.getElementsByClass element "subtitle")

        date-time (java-time/local-date-time
                   (java-time/formatter "EEEE, d MMMM uuuu H:mm")
                   (str
                    (str/replace (.text (first subtitles)) (re-pattern " \\(.*\\)") "")
                    (str/replace (.text (second subtitles)) "Start time" "")))

        route (.text a-element)]

    {:url (.get (.attributes a-element) "href")
     :title (str route ", " (.text (last subtitles)))
     :date-time date-time}))
