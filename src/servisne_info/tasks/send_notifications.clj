(ns servisne-info.tasks.send-notifications
  (:use [servisne-info.notifications :only [send-news-email]]
        [servisne-info.utils :only [fuzzy-has-text?]])
  (:require [servisne-info.repository :as repo]))

; Private

(defn mentiones-user-streets? [user news]
  (some #(fuzzy-has-text? % (:content news)) (:streets user)))

(defn find-news-for-user [user unsent-news]
  [user (filter #(mentiones-user-streets? user %) unsent-news)])

(defn find-news-for-users [users unsent-news]
  (map #(find-news-for-user % unsent-news) users)) 

(defn send-notification [user news]
  (if-not (empty? news)
    (send-news-email user news)))

(defn send-users-notifications [users news]
  (doseq [[user user-news] (find-news-for-users users news)]
    (send-notification user user-news)))

(defn mark-news-as-sent [news]
  (doseq [news-item news]
    (repo/update-news (:url news-item) {:sent true})))

; Public

(defn send-notifications []
  (let [users (repo/find-all-users)
        news (repo/find-unsent-news)]
    (send-users-notifications users news)
    (mark-news-as-sent news)))

(defn -main [& args]
  (repo/db-connect)
  (println "Sending notifications")
  (send-notifications)
  (println "Done")
  (repo/db-disconnect))
