(ns servisne-info.tasks.daily-report
  (:require [servisne-info.notifications :refer [send-daily-report-email]]
            [servisne-info.repository :as repo]
            [servisne-info.tasks.task-definition :refer [deftask]]))

(defn send-daily-report []
  (send-daily-report-email (repo/find-events-for-yesterday)))

(def send-daily-report-task
  (deftask "send daily report"
    (send-daily-report)))
