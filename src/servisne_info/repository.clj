(ns servisne-info.repository
  (:require [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [monger.query :as mq]))

; User
(defn create-user [attributes]
  (mc/insert "users" attributes))

(defn find-user [email]
  (mc/find-one-as-map "users" {:email email}))

(defn find-all-users []
  (map #(from-db-object % true) (mc/find "users")))

(defn update-user [email attributes]
  (mc/update "users" {:email email} {$set attributes}))

; News
(defn create-news [attributes]
  (mc/insert "news" attributes))

(defn find-news [url]
  (mc/find-one-as-map "news" {:url url}))

(defn find-latest-news []
  (mq/with-collection "news"
    (mq/find {})
    (mq/fields [:url :title :created-at])
    (mq/sort (array-map :created-at -1))
    (mq/limit 10)))
