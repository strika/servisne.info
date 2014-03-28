(ns servisne-info.features.registration
  (:use kerodon.core
        kerodon.test
        clojure.test
        servisne-info.features.common)
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [servisne-info.handler :refer [app init]]))

(use-fixtures :once fixtures-once)
(use-fixtures :each fixtures-each)

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
        (fill-in :#email (:email user))
        (press "Sledeći korak →")
        (follow-redirect)
        (fill-in :#streets (:streets user))
        (press "Sačuvaj")
        (within [:h2]
          (has (text? "Podešavanje završeno!")))
        (within [:.email]
          (has (text? "john@example.com")))
        (within [:.streets]
          (has (text? "Mileticeva, Bulevar Oslobodjenja")))
        (assert-user-exists user))))
