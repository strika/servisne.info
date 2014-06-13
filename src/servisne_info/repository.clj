(ns servisne-info.repository
  (:require [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]))

; User
(defn create-user [attributes]
  (mc/insert "users" attributes))

(defn find-user [email]
  (mc/find-one-as-map "users" {:email email}))

(defn find-all-users []
  (map #(from-db-object % true) (mc/find "users")))

(defn update-user [email attributes]
  (mc/update "users" {:email email} {$set attributes}))
