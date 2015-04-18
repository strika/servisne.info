(ns servisne-info.tasks.send-notifications
  (:use [servisne-info.notifications :only [send-news-email]]
        [servisne-info.tasks.task-definition :only [deftask]])
  (:require [servisne-info.logging :as l]
            [servisne-info.repository :as repo]))

; Private
(defn find-news-for-term [term]
  (flatten (map #(repo/search-news %) term)))

(defn find-news-for-user [{:keys [email streets] :as user}]
  (let [street-news (find-news-for-term streets)
        city-wide-news (find-news-for-term ["u celom gradu"])
        unsent-news (filter #(nil? (:sent %)) (concat street-news city-wide-news))]
    (l/info "Finding news for user" {:user email :streets streets :news_count (count unsent-news)})
    [user unsent-news]))

(defn find-news-for-users [users]
  (map find-news-for-user users))

(defn send-notification [user news]
  (if-not (empty? news)
    (do
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
