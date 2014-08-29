(ns servisne-info.tasks
  (:require [overtone.at-at :as at-at]))

(def tasks-pool (at-at/mk-pool :cpu-count 1))
(def default-period (* 60 60 1000)) ; one hour
(def periodic-tasks (atom []))

(defn add-periodic-task [task]
  (swap! periodic-tasks conj task))

(defn schedule-periodic-tasks []
  (doseq [task @periodic-tasks]
    (at-at/every default-period task tasks-pool :initial-delay (/ default-period 10))))
