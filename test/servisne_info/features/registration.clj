(ns servisne-info.features.registration
  (:use clojure.test
        [clojure.string :only [join split]]
        kerodon.core
        kerodon.test
        servisne-info.test-utils)
  (:require [monger.collection :as mc]
            [servisne-info.handler :refer [app init]]
            [servisne-info.repository :as repo]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(defn assert-user-exists [_ user]
  (let [email (:email user)
        streets (:streets user)
        db-user (mc/find-one-as-map "users" {:email email})]
    (is (not (nil? db-user)))
    (is (= (:streets db-user) streets))))

(defn assert-users-count [_ users-count]
  (is (= users-count (count (repo/find-all-users)))))

(deftest registration
  (let [user {:email "john@example.com" :streets ["Mileticeva" "Bulevar Oslobodjenja"]}]
    (-> (session app)
        (visit "/")
        (follow "Prijavi me")
        (fill-in :#email (:email user))
        (press "Sledeći korak →")
        (follow-redirect)
        (fill-in :#streets (join ", " (:streets user)))
        (press "Sačuvaj")
        (within [:h2]
          (has (text? "Podešavanje završeno!")))
        (within [:.email]
          (has (text? "john@example.com")))
        (within [:.streets]
          (has (text? "Mileticeva, Bulevar Oslobodjenja")))
        (assert-user-exists user))))

(deftest update-streets
  (let [user {:email "john@example.com" :streets ["Mileticeva" "Bulevar Oslobodjenja"]}]
    (repo/create-user user)
    (-> (session app)
        (visit "/")
        (follow "Prijavi me")
        (fill-in :#email (:email user))
        (press "Sledeći korak →")
        (follow-redirect)
        (within [:#streets]
          (has (text? (join ", " (:streets user)))))
        (press "Sačuvaj")
        (within [:h2]
          (has (text? "Podešavanje završeno!")))
        (assert-users-count 1))))
