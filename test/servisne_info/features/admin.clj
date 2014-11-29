(ns servisne-info.features.admin
  (:require [clojure.test :refer :all]
            [kerodon.core :refer :all]
            [kerodon.test :refer :all]
            [servisne-info.handler :refer [app]]
            [servisne-info.repository :as repo]
            [servisne-info.test-utils :refer [init-database clean-database]]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(deftest admin-dashboard
  (let [user {:email "john@example.com" :streets ["Bulevar"]}
        power-outage {:title "Power outage" :content "Power outage in Bulevar"}
        event {:message "User created" :email (:email user)}]
    (repo/create-user user)
    (repo/create-news power-outage)
    (repo/create-event event)
    (-> (session app)
        (visit "/admin")
        (within [:#users]
          (has (regex? #"(?s).*john@example.com.*")))
        (within [:#news]
          (has (regex? #"(?s).*Power outage.*")))
        (within [:#events]
          (has (regex? #"(?s).*User created.*"))
          (has (regex? #"(?s).*john@example.com.*"))))))

