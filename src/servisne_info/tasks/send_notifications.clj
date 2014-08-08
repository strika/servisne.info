(ns servisne-info.tasks.send-notifications
  (:use servisne-info.tasks
        [servisne-info.notifications :only [send-news-email]]
        [clojure.string :only [join]])
  (:require [servisne-info.repository :as repo]))

; Private

(defn find-news-for-user [user]
  (let [streets (join " " (:streets user))
        news (repo/search-news streets)
        unsent-news (filter #(nil? (:sent %)) news)]
    (println (str "Finding news for user, user=" (:email user) "news_count=" (count unsent-news)))
    [user unsent-news]))

(defn find-news-for-users [users]
  (map find-news-for-user users))

(defn send-notification [user news]
  (if-not (empty? news)
    (do
      (println (str "Sending news for user, user=" (:email user)))
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

(deftask "send notifications"
  (send-notifications))
