(ns servisne-info.test.repository
  (:use clojure.test
        servisne-info.repository
        servisne-info.test-utils)
  (:require [monger.collection :as mc]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

; Users

(def user {:email "john@example.com" :streets ["Bulevar oslobodjenja" "Dunavska"]})
(def user-2 {:email "mike@example.com" :streets ["Cara Lazara"]})

(deftest test-create-user
  (create-user user)
  (let [saved-user (mc/find-one-as-map @db "users" {:email (:email user)})]
    (is (= (:email saved-user) (:email user)))
    (is (= (:streets saved-user) (:streets user)))
    (is (not (nil? (:created-at saved-user))))))

(deftest test-delete-user
  (create-user user)
  (delete-user (:email user))
  (is (nil? (find-user (:email user)))))

(deftest test-find-user
  (mc/insert @db "users" user)
  (let [saved-user (find-user (:email user))]
    (is (= (:email saved-user) (:email user)))
    (is (= (:streets saved-user) (:streets user)))))

(deftest test-find-all-users
  (mc/insert @db "users" user)
  (mc/insert @db "users" user-2)
  (let [users (find-all-users)
        first-user (first users)
        second-user (second users)]
    (is (= (count users) 2))
    (is (= (:email first-user) (:email user)))
    (is (= (:email second-user) (:email user-2)))))

(deftest test-update-user
  (let [new-attributes {:streets ["Cara Lazara"]}]
    (mc/insert @db "users" user)
    (update-user (:email user) new-attributes)
    (let [saved-user (mc/find-one-as-map @db "users" {:email (:email user)})]
      (is (= (:streets saved-user) (:streets new-attributes))))))

(deftest test-count-users
  (create-user user)
  (create-user user-2)
  (is (= 2 (count-users))))

; News
(def power-outage {:title "Power outage"
                   :url "http://example.com/power_outage"
                   :content "Power outage in Dunavska street"})

(def old-news {:title "Old news"
               :url "http://example.com/old_news"
               :sent true})

(deftest test-create-news
  (create-news power-outage)
  (let [saved-news (mc/find-one-as-map @db "news" {:url (:url power-outage)})]
    (is (= (:title saved-news) (:title power-outage)))
    (is (= (:url saved-news) (:url saved-news)))
    (is (not (nil? (:created-at saved-news))))))

(deftest test-find-news
  (mc/insert @db "news" power-outage)
  (let [saved-news (find-news (:url power-outage))]
    (is (= (:title saved-news) (:title power-outage)))
    (is (= (:url saved-news) (:url power-outage)))))

(deftest test-update-news
  (let [new-attributes {:sent true}]
    (mc/insert @db "news" power-outage)
    (update-news (:url power-outage) new-attributes)
    (let [saved-news (mc/find-one-as-map @db "news" {:url (:url power-outage)})]
      (is (= (:sent saved-news) (:sent new-attributes))))))

(deftest test-find-latest-news
  (let [closed-street {:title "Closed street" :url "http://example.com/closed_street"}]
    (mc/insert @db "news" power-outage)
    (mc/insert @db "news" closed-street)
    (is (= (count (find-latest-news)) 2))))

(deftest test-find-unsent-news
  (mc/insert @db "news" power-outage)
  (mc/insert @db "news" old-news)
  (let [unsent-news (find-unsent-news)]
    (is (= 1 (count unsent-news)))
    (is (= (:url power-outage) (:url (first unsent-news))))))

(deftest test-search-news
  (testing "search term with one word"
    (mc/insert @db "news" power-outage)
    (let [result (search-news "Dunavska")]
      (is (= (:url (first result)) (:url power-outage)))))

  (testing "search term with two words"
    (let [power-outage {:title "Power outage"
                        :url "http://example.com/power_outage"
                        :content "Power outage in 1300 kaplara street"}]
      (mc/insert @db "news" power-outage)
      (let [result (search-news "1300 kaplara")]
        (is (= (:url (first result)) (:url power-outage))))))

  (testing "search term with similar words"
    (let [closed-road {:title "Closed road"
                       :url "http://example.com/closed_road"
                       :content "Closed road in Svetozara Ćorovića."}]
      (mc/insert @db "news" closed-road)
      (is (empty? (search-news "Svetozara Miletića"))))))

; Events

(def email-sent {:message "Email sent"
                 :description "email='john@example.com' streets='Some street'"})

(deftest test-create-event
  (create-event email-sent)
  (let [saved-event (mc/find-one-as-map @db "events" {:message (:message email-sent)})]
    (is (= (:description saved-event) (:description email-sent)))
    (is (not (nil? (:created-at saved-event))))))

(deftest test-find-all-events
  (create-event email-sent)
  (let [events (find-all-events)]
    (is (= 1 (count events)))))

(deftest test-find-latest-events
  (create-event email-sent)
  (let [events (find-latest-events)]
    (is (= 1 (count events)))))

(deftest test-find-events-for-yesterday
  (create-event email-sent)
  (let [events (find-events-for-yesterday)]
    (is (= 1 (count events)))
    (is (= (:message email-sent) (:message (first events))))))
