(ns servisne-info.controllers.admin
  (:require [servisne-info.logging :refer [format-data]]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn- format-event [event]
  {:message (:message event)
   :created-at (:created-at event)
   :data (format-data (dissoc event :id :message :created-at))})

(defn dashboard []
  (let [events (map format-event (repo/find-all-events))]
    (layout/render
      "admin/dashboard.html"
      {:users (repo/find-all-users)
       :news (repo/find-latest-news)
       :events events})))
