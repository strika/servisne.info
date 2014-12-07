(ns servisne-info.notifications
  (:use postal.core)
  (:require [clj-time.core :as t]
            [environ.core :refer [env]]
            [servisne-info.event :as event]
            [servisne-info.views.email :as email]))

(def default-from "info@servisne.info")

(defn send-email
  [to subject body]
  (send-message {:host (env :email-host)
                 :user (env :email-user) 
                 :pass (env :email-pass)
                 :port 587}
                {:from default-from
                 :to to
                 :subject subject
                 :body body}))

(defn send-news-email [user news]
  (event/record "Sending news email" {:user (:email user)})
  (send-email (:email user)
              "Servisne informacije"
              (email/render "new_links.txt"
                            {:links (map :url news)})))

(defn send-daily-report-email [events]
  (send-email (env :email-host)
              "[servisne.info] Dnevni izveštaj"
              (email/render "daily_report.txt"
                            {:date (t/now)
                             :events events})))
