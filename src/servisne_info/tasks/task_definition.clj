(ns servisne-info.tasks.task-definition
  (:use [environ.core :only [env]]
        [raven-clj.core :only [capture]]
        [raven-clj.interfaces :only [stacktrace]])
  (:require [servisne-info.logging :as l]))

(defmacro deftask [task-name & body]
  `(fn []
     (try
       (do
         (l/info "Task start" {:task ~task-name})
         ~@body
         (l/info "Task done" {:task ~task-name}))
       (catch Exception e#
         (capture (env :sentry-dsn)
                  (-> {:message (.getMessage e#)}
                      (stacktrace e#)))))))
