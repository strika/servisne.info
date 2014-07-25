(ns servisne-info.tasks
  (:use [raven-clj.core :only [capture]]
        [raven-clj.interfaces :only [stacktrace]])
  (:require [environ.core :refer [env]]))

(defmacro deftask [task-name & body]
  `(defn ~'-main [& args#]
     (try
       (do
         (println ~task-name " starting...")
         (servisne-info.repository/db-connect)
         ~@body
         (servisne-info.repository/db-disconnect)
         (println ~task-name " done."))
       (catch Exception e#
         (capture (env :sentry-dsn)
                  (-> {:message (.getMessage e#)}
                      (stacktrace e#)))))))
