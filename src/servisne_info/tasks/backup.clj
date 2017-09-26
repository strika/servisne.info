(ns servisne-info.tasks.backup
  (:require [aws.sdk.s3 :as s3]
            [clj-time.core :refer [now]]
            [clj-time.format :as f]
            [clojure.java.shell :refer [sh]]
            [environ.core :refer [env]]
            [servisne-info.tasks.task-definition :refer [deftask]]))

(def s3-credentials {:access-key (env :s3-access-key)
                     :secret-key (env :s3-secret-key)})

(def s3-bucket "servisne.info")

(def users-path (str (env :data-dir) "/users.json"))

(defn users-backup-name []
  (let [formatter (f/formatters :date-hour)]
    (str "users-" (f/unparse formatter (now)) ".json")))

(defn backup-users []
  (s3/put-object s3-credentials
                 s3-bucket
                 (users-backup-name)
                 (clojure.java.io/file users-path)))

(def backup-task
  (deftask "backup"
    (sh "scripts/export-users.sh")
    (backup-users)))
