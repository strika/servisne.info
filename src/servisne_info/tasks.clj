(ns servisne-info.tasks
  (:require [overtone.at-at :as at-at]
            [servisne-info.tasks.backup :refer [backup-task]]
            [servisne-info.tasks.daily-report :refer [send-daily-report-task]]
            [servisne-info.tasks.scrape :refer [scrape-task]]
            [servisne-info.tasks.send-notifications :refer [send-notifications-task]]
            [servisne-info.utils :refer [production?]]))

(def tasks-pool (at-at/mk-pool :cpu-count 1))
(def default-period (* 60 60 1000)) ; one hour
(def default-delay (/ default-period 10))
(def periodic-tasks (atom []))

(defn add-periodic-task
  ([task] (add-periodic-task task default-period))
  ([task period]
   (swap! periodic-tasks conj {:task task :period period})))

(defn schedule-periodic-tasks []
  (doseq [{:keys [task period]} @periodic-tasks]
    (at-at/every period task tasks-pool :initial-delay default-delay)))

(if production?
  (do
    (add-periodic-task backup-task (* default-period 6))
    (add-periodic-task scrape-task)
    (add-periodic-task send-notifications-task)
    (add-periodic-task send-daily-report-task (* default-period 24))))
