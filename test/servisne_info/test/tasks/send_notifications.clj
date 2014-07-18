(ns servisne-info.test.tasks.send-notifications
  (:use clojure.test
        servisne-info.test-utils
        servisne-info.notifications
        servisne-info.tasks.send-notifications)
  (:require [monger.collection :as mc]
            [environ.core :refer [env]]
            [servisne-info.repository :as repo]))

(use-fixtures :once (compose-fixtures stub-emails init-database))
(use-fixtures :each (compose-fixtures clean-emails clean-database))

(def user {:email "nebojsa.stricevic@gmail.com" :streets ["Bulevar oslobodjenja" "Dunavska"]})
(def power-outage {:title "Power outage" :url "http://example.com/power_outage" :content "Power outage in Dunavska"})
(def closed-road {:title "Closed road" :url "http://example.com/closed_road" :content "Closed road somewhere"})

(deftest find-news-for-user-test
  (repo/create-user user)
  (repo/create-news power-outage)
  (repo/create-news closed-road)
  (let [result (find-news-for-user user)
        result-user (first result)
        result-news (second result)]
    (is (= result-user user))
    (is (= (:url (first result-news)) (:url power-outage)))))

(deftest send-notifications-test
  (repo/create-user user)
  (repo/create-news power-outage)
  (repo/create-news closed-road)
  (send-notifications)
  (is (= 1 (count @sent-emails)))
  (is (= (:email user) (:to (first @sent-emails))))
  (is (= 0 (count (repo/find-unsent-news)))))
