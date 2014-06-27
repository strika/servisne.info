(ns servisne-info.repository
  (:require [environ.core :refer [env]]
            [monger.core :as monger]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [monger.query :as mq]
            [taoensso.timbre :as timbre]))

; Database configuration
(defn db-connect []
  (let [host     (or (env :openshift-mongodb-db-host) "localhost")
        port     (Integer/parseInt (or (env :openshift-mongodb-db-port) "27017"))
        username (or (env :openshift-mongodb-db-username) "")
        password (or (env :openshift-mongodb-db-password) "")
        db       (or (env :openshift-app-name) (and (= (env :clj-env) "test") "servisne-test") "servisne")]
    (timbre/info (str "servisne-info connecting to MongoDB: " host ":" port "/" db))
    (monger/connect! {:host host :port port})
    (monger/use-db! db)
    (monger/authenticate (monger/get-db db) username (.toCharArray password))))

(defn db-disconnect []
  (monger/disconnect!))

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
(def news-attributes [:url :title :content :sent :created-at])

(defn create-news [attributes]
  (mc/insert "news" attributes))

(defn find-news [url]
  (mc/find-one-as-map "news" {:url url}))

(defn update-news [url attributes]
  (mc/update "news" {:url url} {$set attributes}))

(defn find-latest-news []
  (mq/with-collection "news"
    (mq/find {})
    (mq/fields news-attributes)
    (mq/sort (array-map :created-at -1))
    (mq/limit 10)))

(defn find-unsent-news []
  (mq/with-collection "news"
    (mq/find {:sent nil})
    (mq/fields news-attributes)))
