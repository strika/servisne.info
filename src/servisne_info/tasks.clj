(ns servisne-info.tasks
  (:use [raven-clj.core :only [capture]]
        [raven-clj.interfaces :only [stacktrace]])
  (:require [environ.core :refer [env]]
            [overtone.at-at :as at-at]
            [taoensso.timbre :as timbre]))

(defmacro deftask [task-name & body]
  `(fn []
     (try
       (do
         (timbre/info ~task-name " starting...")
         ~@body
         (timbre/info ~task-name " done."))
       (catch Exception e#
         (capture (env :sentry-dsn)
                  (-> {:message (.getMessage e#)}
                      (stacktrace e#)))))))

(def tasks-pool (at-at/mk-pool :cpu-count 1))
(def default-period (* 60 60 1000)) ; one hour
(def periodic-tasks (atom []))

(defn add-periodic-task [task]
  (swap! periodic-tasks conj task))

(defn schedule-periodic-tasks []
  (doseq [task @periodic-tasks]
    (at-at/every default-period task tasks-pool :initial-delay (/ default-period 10))))
