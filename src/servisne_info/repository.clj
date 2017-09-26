(ns servisne-info.repository
  (:require monger.joda-time
            [clj-time.core :as t]
            [clojure.string :refer [split]]
            [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [monger.query :as mq]
            [monger.search :as ms]
            [servisne-info.logging :as l]))

(def connection (atom nil))
(def db (atom nil))

(defn- add-timestamp [attributes]
  (assoc attributes :created-at (t/now)))

; Database configuration
(defn db-connect []
  (l/info "Connecting to MongoDB")
  (let [uri (or (env :mongodb-uri) "mongodb://127.0.0.1/servisneinfo")
        connection-and-db (mg/connect-via-uri uri)]
    (reset! connection (:conn connection-and-db))
    (reset! db (:db connection-and-db))
    (mc/ensure-index @db "news" (array-map :content "text"))
    (l/info "Connected")))

(defn db-disconnect []
  (mg/disconnect @connection))

; User
(defn create-user [attributes]
  (mc/insert @db "users" (add-timestamp attributes)))

(defn delete-user [email]
   (mc/remove @db "users" {:email email}))

(defn find-user [email]
  (mc/find-one-as-map @db "users" {:email email}))

(defn find-all-users []
  (map #(from-db-object % true) (mc/find @db "users")))

(defn update-user [email attributes]
  (mc/update @db "users" {:email email} {$set attributes}))

(defn remove-users []
  (mc/remove @db "users"))

(defn count-users []
  (mc/count @db "users"))

; News
(def news-attributes [:url :title :content :sent :created-at])

(defn create-news [attributes]
  (mc/insert @db "news" (add-timestamp attributes)))

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

(defn escape-search-term [search]
  (if (> (count (split search #" ")) 1)
    (str "\"" search "\"")
    search))

(defn search-news [search]
  (map :obj
    (ms/results-from
      (ms/search @db "news" (escape-search-term search)))))

(defn find-unsent-news []
  (mq/with-collection @db "news"
    (mq/find {:sent nil})
    (mq/fields news-attributes)))

(defn remove-news []
  (mc/remove @db "news"))

; Events

(defn create-event [attributes]
  (mc/insert @db "events" (add-timestamp attributes)))

(defn find-all-events []
  (map #(from-db-object % true) (mc/find @db "events")))

(defn find-latest-events []
  (mq/with-collection @db "events"
    (mq/find {})
    (mq/sort (array-map :created-at -1))
    (mq/limit 30)))

(defn find-events-for-yesterday []
  (let [today (t/plus (t/now) (t/minutes 60))
        yesterday (t/minus today (t/days 1))]
    (map #(from-db-object % true)
         (mc/find @db "events" {$and [{:created-at {$gt yesterday}}
                                      {:created-at {$lte today}}]}))))

(defn remove-events []
  (mc/remove @db "events"))

; All

(defn remove-all []
  (remove-users)
  (remove-news)
  (remove-events))
