(ns servisne-info.controllers.users
  (:require [clojure.string :refer [join trim split lower-case]]
            [noir.response :as response]
            [servisne-info.event :as event]
            [servisne-info.notifications :as notifications]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]
            [servisne-info.validations :refer [user-valid?]]))

(defn- split-streets [streets]
  (map trim (split streets #",")))

(defn users-delete [email]
  (layout/render "users/delete.html" {:email email}))

(defn users-destroy [email]
  (let [user (repo/find-user email)]
    (if user
      (do
        (event/record "Removing user" {:email email})
        (repo/delete-user email)))
    (layout/render "users/destroy.html" {:user user})))

(defn create-or-update-user [email streets]
  (let [streets-seq (split-streets streets)]
    (if (repo/find-user email)
      (repo/update-user email {:streets streets-seq})
      (let [user {:email email :streets streets-seq}]
        (if (user-valid? user)
          (do
            (repo/create-user user)
            (notifications/send-registration-confirmation-email user)
            (event/record "User signed up" {:email email})))))))

(defn users-create [email streets river]
  (if (and email (= (lower-case river) "dunav"))
    (create-or-update-user email streets))
  (layout/render "users/create.html"
                 {:email email :streets streets}))
