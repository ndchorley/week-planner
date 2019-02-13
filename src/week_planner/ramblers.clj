(ns week-planner.ramblers
  (:require [clojure.string :as str]
            [java-time]
            [clj-http.client :as http-client]
            [hickory.core :as hickory]
            [hickory.select :as hickory-select]))

(declare to-event)

(defn get-events []
  (mapcat
   (fn [gid]
     (let [document
           (hickory/as-hickory
            (hickory/parse
             (:body
              (http-client/get (str "https://www.ramblers.org.uk/find-a-walk.aspx?layer=walks&tab=walks&group=" gid)))))

           elements (hickory-select/select (hickory-select/class "details") document)]
     (map to-event elements)))
   #{"IL50" "ES50"}))

(defn- to-event [element]
  (let [a-element (first (hickory-select/select (hickory-select/tag :a) element))
        route (first (:content a-element))
        subtitles (hickory-select/select (hickory-select/class "subtitle") element)

        date-time (java-time/local-date-time
                   (java-time/formatter "EEEE, d MMMM uuuu H:mm")
                   (str
                    (str/replace (str/trim (first (:content (first subtitles)))) (re-pattern " \\(.*\\)") "")
                    (str/replace (str/trim (first (:content (second subtitles)))) "Start time" "")))]

    {:url (:href (:attrs a-element))
     :title (str
             route
             ", "
             (str/join ", " (map str/trim (str/split-lines (str/trim (first (:content (last subtitles))))))))
     :date-time date-time}))
