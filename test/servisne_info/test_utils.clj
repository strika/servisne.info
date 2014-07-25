(ns servisne-info.test-utils
  (:require [servisne-info.repository :as repo]
            [servisne-info.handler :refer [init]]
            [servisne-info.notifications :as notifications]))

(def sent-emails (atom []))

(defn stub-send-email [to subject body]
  (swap! sent-emails conj {:to to :subject subject :body body}))

(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))

(defn stub-emails [t]
  (with-redefs [notifications/send-email stub-send-email]
    (t)))

(defn init-database [t]
  (init)
  (t))

(defn clean-database [t]
  (repo/remove-all)
  (t))

(defn clean-emails [t]
  (reset! sent-emails [])
  (t))