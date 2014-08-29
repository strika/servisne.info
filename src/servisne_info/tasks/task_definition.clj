(ns servisne-info.tasks.task-definition
  (:use [environ.core :only [env]]
        [raven-clj.core :only [capture]]
        [raven-clj.interfaces :only [stacktrace]])
  (:require [taoensso.timbre :as timbre]))

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
