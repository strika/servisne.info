(ns servisne-info.routes.users
  (:use compojure.core
        [clojure.string :only [join trim split]])
  (:require [noir.response :as response]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn users-delete []
  (layout/render "users/delete.html"))

(defn split-streets [streets]
  (map trim (split streets #",")))

(defn users-create [email streets]
  (let [streets-seq (split-streets streets)]
    (if (repo/find-user email)
      (repo/update-user email {:streets streets-seq})
      (repo/create-user {:email email :streets streets-seq})))
  (layout/render "users/create.html"
                 {:email email :streets streets}))

(defroutes users-routes
  (GET "/users/delete" [] (users-delete))
  (POST "/users/create" [email streets] (users-create email streets)))
