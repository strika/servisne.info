(ns servisne-info.features.unsubscribe
  (:use clojure.test
        kerodon.core
        kerodon.test
        servisne-info.test-utils)
  (:require [servisne-info.handler :refer [app init]]
            [servisne-info.repository :as repo]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(defn assert-user-does-not-exist [_ user]
  (is (nil? (repo/find-user (:email user)))))

(deftest unsubscribe
  (let [user {:email "john@example.com" :streets ["Mileticeva" "Bulevar Oslobodjenja"]}]
    (repo/create-user user)
    (-> (session app)
        (visit "/users/delete")
        (fill-in :#email (:email user))
        (press "Odjavi me")
        (within [:h2]
          (has (text? "Odjavljivanje završeno")))
        (assert-user-does-not-exist user))))
