(ns servisne-info.features.registration
  (:use clojure.test
        [clojure.string :only [join split]]
        kerodon.core
        kerodon.test
        servisne-info.test-utils)
  (:require [servisne-info.handler :refer [app init]]
            [servisne-info.repository :as repo]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(defn assert-user-exists [_ user]
  (let [email (:email user)
        streets (:streets user)
        db-user (repo/find-user email)]
    (is (not (nil? db-user)))
    (is (= (:streets db-user) streets))))

(defn assert-users-count [_ users-count]
  (is (= users-count (count (repo/find-all-users)))))

(deftest registration
  (let [user {:email "john@example.com" :streets ["Mileticeva" "Bulevar Oslobodjenja"]}]
    (-> (session app)
        (visit "/")
        (fill-in :#email (:email user))
        (fill-in :#streets (join ", " (:streets user)))
        (press "Prijavi me")
        (within [:h2]
          (has (text? "Uspešno ste se prijavili!")))
        (within [:.email]
          (has (text? "john@example.com")))
        (within [:.streets]
          (has (text? "Mileticeva, Bulevar Oslobodjenja")))
        (assert-user-exists user))))

(deftest empty-registration
  (-> (session app)
      (visit "/")
      (press "Prijavi me")
      (within [:h2]
        (has (text? "Prijava nije uspela!")))))
