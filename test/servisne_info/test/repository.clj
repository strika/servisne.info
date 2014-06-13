(ns servisne-info.test.repository
  (:use clojure.test
        servisne-info.repository
        servisne-info.features.common)
  (:require [monger.collection :as mc]))

(use-fixtures :once fixtures-once)
(use-fixtures :each fixtures-each)

; User
(def user {:email "john@example.com" :streets ["Bulevar oslobodjenja" "Dunavska"]})

(deftest test-create-user
  (create-user user)
  (let [saved-user (mc/find-one-as-map "users" {:email (:email user)})]
    (is (= (:email saved-user) (:email user)))
    (is (= (:streets saved-user) (:streets user)))))

(deftest test-find-user
  (mc/insert "users" user)
  (let [saved-user (find-user (:email user))]
    (is (= (:email saved-user) (:email user))
        (= (:streets saved-user) (:streets user)))))

(deftest test-find-all-users
  (let [user-2 {:email "mike@example.com" :streets ["Cara Lazara"]}]
    (mc/insert "users" user)
    (mc/insert "users" user-2)
    (let [users (find-all-users)
          first-user (first users)
          second-user (second users)]
      (is (= (count users) 2))
      (is (= (:email first-user) (:email user)))
      (is (= (:email second-user) (:email user-2))))))

(deftest test-update-user
  (let [new-attributes {:streets ["Cara Lazara"]}]
    (mc/insert "users" user)
    (update-user (:email user) new-attributes)
    (let [saved-user (mc/find-one-as-map "users" {:email (:email user)})]
      (is (= (:streets saved-user) (:streets new-attributes))))))
