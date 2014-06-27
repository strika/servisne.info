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

(deftest mentiones-user-streets-test
  (is (mentiones-user-streets? user power-outage)))

(deftest find-news-for-user-test
  (let [news (find-news-for-user user [power-outage closed-road])]
    (is (= (first news) user))
    (is (= (second news) [power-outage]))))

(deftest send-notifications-test
  (repo/create-user user)
  (repo/create-news power-outage)
  (repo/create-news closed-road)
  (send-notifications)
  (is (= 1 (count @sent-emails)))
  (is (= (:email user) (:to (first @sent-emails)))))
