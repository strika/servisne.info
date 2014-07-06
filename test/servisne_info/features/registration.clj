(ns servisne-info.features.registration
  (:use kerodon.core
        kerodon.test
        clojure.test
        servisne-info.test-utils
        [clojure.string :only [join split]])
  (:require [monger.collection :as mc]
            [servisne-info.handler :refer [app init]]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(defn assert-user-exists [_ user]
  (let [email (:email user)
        streets (:streets user)
        db-user (mc/find-one-as-map "users" {:email email})]
    (is (not (nil? db-user)))
    (is (= (:streets db-user) streets))))

(defn create-user [user]
  (mc/insert "users" user))

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
  (let [user {:email "john@example.com" :streets "Mileticeva, Bulevar Oslobodjenja"}]
    (create-user user)
    (-> (session app)
        (visit "/")
        (follow "Prijavi me")
        (fill-in :#email (:email user))
        (press "Sledeći korak →")
        (follow-redirect)
        (within [:#streets]
          (has (text? (:streets user)))))))
