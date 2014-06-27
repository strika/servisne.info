(ns servisne-info.test-utils
  (:require [monger.collection :as mc]
            [servisne-info.handler :refer [init]]))


(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))

(defn init-database [t]
  (init)
  (t))

(defn clean-database [t]
  (mc/remove "users")
  (mc/remove "news")
  (t))
