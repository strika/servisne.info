(ns servisne-info.test.tasks.daily-report
  (:require
    [clojure.test :refer :all]
    [servisne-info.repository :as repo]
    [servisne-info.tasks.daily-report :refer [send-daily-report]]
    [servisne-info.test-utils :refer :all]))

(use-fixtures :once (compose-fixtures stub-emails init-database))
(use-fixtures :each (compose-fixtures clean-emails clean-database))

(def email-sent {:message "Email sent"
                 :email "john@example.com"
                 :streets "Bulevar"})

(def user {:email "john@example.com" :streets ["Bulevar oslobodjenja"]})
(def user-2 {:email "mike@example.com" :streets ["Cara Lazara"]})

(deftest send-daily-report-test
  (repo/create-user user)
  (repo/create-user user-2)
  (repo/create-event email-sent)
  (send-daily-report)
  (let [email (first @sent-emails)]
    (is email)
    (is (.contains (:body email) "Broj korisnika: 2"))
    (is (.contains (:body email) (:message email-sent)))
    (is (.contains (:body email) (:email email-sent)))
    (is (.contains (:body email) (:streets email-sent)))))
