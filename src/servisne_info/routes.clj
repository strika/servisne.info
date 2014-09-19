(ns servisne-info.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [noir.util.route :refer [restricted]]
            [servisne-info.controllers.admin :as admin]
            [servisne-info.controllers.pages :as pages]
            [servisne-info.controllers.users :as users]))

(defn admin-access [request]
  (let [token (:token (:params request))]
    (= token (env :admin-token))))

(defroutes servisne-info-routes
  (GET "/admin" [] (restricted (admin/dashboard)))
  (GET "/about" [] (pages/about-page))
  (GET "/terms" [] (pages/terms-page))
  (POST "/users/create" [email streets] (users/users-create email streets))
  (GET "/users/delete" [] (users/users-delete))
  (POST "/users/destroy" [email] (users/users-destroy email))
  (GET "/" [] (pages/home-page))
  (route/resources "/")
  (route/not-found "Not Found"))
