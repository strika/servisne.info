(ns servisne-info.persistence
  (:require [monger.core :as monger]
            [environ.core :refer [env]]
            [taoensso.timbre :as timbre]))

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
