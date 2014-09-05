(ns servisne-info.test.logging
  (:use clojure.test)
  (:require [servisne-info.logging :as l]))

(deftest format-data-test
  (let [data (l/format-data {:username "john" :email "john@example.com"})]
    (is (= data
           " username='john' email='john@example.com'"))))

(deftest format-message-test
  (let [message (l/format-message "New user" {:email "john@example.com"})]
  (is (= message
         "New user, email='john@example.com'"))))
