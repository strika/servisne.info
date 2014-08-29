(ns servisne-info.tasks.schedule-tasks
  (:require [servisne-info.tasks :as tasks]
            [servisne-info.tasks.scrape :as scrape]
            [servisne-info.tasks.send-notifications :as send-notifications]))

(defn schedule-tasks []
  (tasks/schedule-periodic-tasks))
