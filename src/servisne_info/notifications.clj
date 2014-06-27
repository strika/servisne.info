(ns servisne-info.notifications
  (:use postal.core)
  (:require [environ.core :refer [env]]
            [servisne-info.views.email :as email]))

(def default-from "nebojsa.stricevic+servisne@gmail.com")

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
  (send-email (:email user)
              "Servisne informacije"
              (email/render "new_links.txt"
                            {:links (map :url news)})))

(defn send-test-email []
  (send-email "nebojsa.stricevic@gmail.com"
              "Test poruka"
              (email/render "new_links.txt"
                            {:links ["http://servisne.info/" "http://servisne.info/about"]})))
