(ns servisne-info.tasks.daily-report
  (:require [servisne-info.logging :as l]
            [servisne-info.notifications :refer [send-daily-report-email]]
            [servisne-info.repository :as repo]
            [servisne-info.tasks.task-definition :refer [deftask]]))

(defn- format-event-data [event]
  (l/format-data (dissoc event :_id)))

(defn send-daily-report []
  (let [events (repo/find-events-for-yesterday)]
    (send-daily-report-email (map format-event-data events))))

(def send-daily-report-task
  (deftask "send daily report"
    (send-daily-report)))
