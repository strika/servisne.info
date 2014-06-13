(ns servisne-info.routes.users
  (:use compojure.core)
  (:require [noir.response :as response]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn- create-user [email]
  (repo/create-user {:email email :streets {}}))

(defn users-new []
  (layout/render "users/new.html"))

(defn users-create [email]
  (create-user email)
  (response/redirect (str "/streets/edit?email=" email)))

(defroutes users-routes
  (GET "/users/new" [] (users-new))
  (POST "/users/create" [email] (users-create email)))
