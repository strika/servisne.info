(ns servisne-info.repository
  (:require [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [monger.query :as mq]
            [monger.search :as ms]
            [taoensso.timbre :as timbre]))

(def connection (atom nil))
(def db (atom nil))

; Database configuration
(defn db-connect []
  (let [host     (or (env :openshift-mongodb-db-host) "localhost")
        port     (Integer/parseInt (or (env :openshift-mongodb-db-port) "27017"))
        username (or (env :openshift-mongodb-db-username) "")
        password (or (env :openshift-mongodb-db-password) "")
        db-name  (or (env :openshift-app-name) (and (= (env :clj-env) "test") "servisne-test") "servisne")]
    (timbre/info (str "Connecting to MongoDB: " host ":" port "/" db-name))
    (reset! connection (mg/connect {:host host :port port}))
    (reset! db (mg/get-db @connection db-name))
    (mg/authenticate @db username (.toCharArray password))
    (mc/ensure-index @db "news" (array-map :content "text"))
    (timbre/info "Connected")))

(defn db-disconnect []
  (mg/disconnect @connection))

; User
(defn create-user [attributes]
  (mc/insert @db "users" attributes))

(defn find-user [email]
  (mc/find-one-as-map @db "users" {:email email}))

(defn find-all-users []
  (map #(from-db-object % true) (mc/find @db "users")))

(defn update-user [email attributes]
  (mc/update @db "users" {:email email} {$set attributes}))

(defn remove-users []
  (mc/remove @db "users"))

; News
(def news-attributes [:url :title :content :sent :created-at])

(defn create-news [attributes]
  (mc/insert @db "news" attributes))

(defn find-news [url]
  (mc/find-one-as-map @db "news" {:url url}))

(defn update-news [url attributes]
  (mc/update @db "news" {:url url} {$set attributes}))

(defn find-all-news []
  (map #(from-db-object % true) (mc/find @db "news")))

(defn find-latest-news []
  (mq/with-collection @db "news"
    (mq/find {})
    (mq/fields news-attributes)
    (mq/sort (array-map :created-at -1))
    (mq/limit 10)))

(defn search-news [search]
  (map :obj
    (ms/results-from
      (ms/search @db "news" search))))

(defn find-unsent-news []
  (mq/with-collection @db "news"
    (mq/find {:sent nil})
    (mq/fields news-attributes)))

(defn remove-news []
  (mc/remove @db "news"))

; All

(defn remove-all []
  (remove-users)
  (remove-news))
