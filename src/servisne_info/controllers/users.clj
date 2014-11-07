(ns servisne-info.controllers.users
  (:require [clojure.string :refer [join trim split]]
            [noir.response :as response]
            [servisne-info.logging :as l]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn- split-streets [streets]
  (map trim (split streets #",")))

(defn users-delete []
  (layout/render "users/delete.html"))

(defn users-destroy [email]
  (if (repo/find-user email)
    (do
      (l/info "Removing user" {:email email})
      (repo/delete-user email)))
  (layout/render "users/destroy.html"))

(defn create-or-update-user [email streets]
  (let [streets-seq (split-streets streets)]
    (if (repo/find-user email)
      (repo/update-user email {:streets streets-seq})
      (repo/create-user {:email email :streets streets-seq}))))

(defn users-create [email streets]
  (if email
    (create-or-update-user email streets))
  (layout/render "users/create.html"
                 {:email email :streets streets}))
