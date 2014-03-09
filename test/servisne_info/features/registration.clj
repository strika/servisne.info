(ns servisne-info.features.registration
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [servisne-info.handler :refer [app init]]))

(use-fixtures :once
  (fn [t]
    (init)
    (t)))

(use-fixtures :each
  (fn [t]
    (mc/remove "users")
    (t)))

(defn assert-user-exists [_ user]
  (let [email (:email user)
        streets (:streets user)
        db-user (mc/find-one-as-map "users" {:email email})]
    (is (not (nil? db-user)))
    (is (= (:streets db-user) streets))))

(deftest registration
  (let [user {:email "john@example.com" :streets "Mileticeva, Bulevar Oslobodjenja"}]
    (-> (session app)
        (visit "/")
        (follow "Prijavi me")
        (fill-in "Email:" (:email user))
        (press "Dodaj ulice")
        (follow-redirect)
        (fill-in "Ulice:" (:streets user))
        (press "Sačuvaj")
        (within [:h2]
          (has (text? "Uspešno ste sačuvali ulice")))
        (assert-user-exists user))))
