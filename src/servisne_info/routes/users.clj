(ns servisne-info.routes.users
  (:use compojure.core
        [clojure.string :only [join trim split]])
  (:require [noir.response :as response]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn- create-user [email]
  (repo/create-user {:email email :streets {}}))

(defn users-new []
  (layout/render "users/new.html"))

(defn users-edit [email]
  (let [user (update-in (repo/find-user email)
                        [:streets]
                        #(join ", " %))]
    (layout/render "users/edit.html" user)))

(defn users-update [email streets]
  (repo/update-user email {:streets (map trim (split streets #","))})
  (layout/render "users/update.html"
                 {:email email :streets streets}))

(defn users-create [email]
  (create-user email)
  (response/redirect (str "/users/edit?email=" email)))

(defroutes users-routes
  (GET "/users/new" [] (users-new))
  (GET "/users/edit" [email] (users-edit email))
  (POST "/users/update" [email streets] (users-update email streets))
  (POST "/users/create" [email] (users-create email)))
