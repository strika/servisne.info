(ns servisne-info.tasks.send-notifications
  (:use [clojure.string :only [join split]]
        [servisne-info.notifications :only [send-news-email]]
        [servisne-info.tasks.task-definition :only [deftask]])
  (:require [servisne-info.logging :as l]
            [servisne-info.repository :as repo]))

; Private
(defn escape-street [street]
  (if (> (count (split street #" ")) 1)
    (str "\\\"" street "\\\"")
    street))

(defn escape-streets [user]
  (map escape-street (:streets user)))

(defn find-news-for-user [user]
  (let [streets (escape-streets user)
        news (flatten (map #(repo/search-news %) streets))
        unsent-news (filter #(nil? (:sent %)) news)]
    (l/info "Finding news for user" {:user (:email user) :streets streets :news_count (count unsent-news)})
    [user unsent-news]))

(defn find-news-for-users [users]
  (map find-news-for-user users))

(defn send-notification [user news]
  (if-not (empty? news)
    (do
      (l/info "Sending news for user" {:user (:email user)})
      (send-news-email user news))))

(defn send-users-notifications [users]
  (doseq [[user user-news] (find-news-for-users users)]
    (send-notification user user-news)))

(defn mark-news-as-sent [news]
  (doseq [news-item news]
    (repo/update-news (:url news-item) {:sent true})))

; Public

(defn send-notifications []
  (send-users-notifications (repo/find-all-users))
  (mark-news-as-sent (repo/find-unsent-news)))

(def send-notifications-task
  (deftask "send notifications"
    (send-notifications)))
