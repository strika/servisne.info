(ns servisne-info.test.tasks.daily-report
  (:require
    [clojure.test :refer :all]
    [servisne-info.repository :as repo]
    [servisne-info.tasks.daily-report :refer [send-daily-report]]
    [servisne-info.test-utils :refer :all]))

(use-fixtures :once (compose-fixtures stub-emails init-database))
(use-fixtures :each (compose-fixtures clean-emails clean-database))

(def email-sent {:message "Email sent"
                 :description "email='john@example.com' streets='Some street'"})

(deftest send-daily-report-test
  (repo/create-event email-sent)
  (send-daily-report)
  (is (= 1 (count @sent-emails))))
