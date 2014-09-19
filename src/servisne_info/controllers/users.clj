(ns servisne-info.controllers.users
  (:require [clojure.string :refer [join trim split]]
            [noir.response :as response]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn- split-streets [streets]
  (map trim (split streets #",")))

(defn users-delete []
  (layout/render "users/delete.html"))

(defn users-destroy [email]
  (if (repo/find-user email)
    (repo/delete-user email))
  (layout/render "users/destroy.html"))

(defn users-create [email streets]
  (let [streets-seq (split-streets streets)]
    (if (repo/find-user email)
      (repo/update-user email {:streets streets-seq})
      (repo/create-user {:email email :streets streets-seq})))
  (layout/render "users/create.html"
                 {:email email :streets streets}))
