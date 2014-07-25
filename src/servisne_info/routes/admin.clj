(ns servisne-info.routes.admin
  (:use compojure.core
        [noir.util.route :only [restricted]])
  (:require [environ.core :refer [env]]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn admin-access [request]
  (let [token (:token (:params request))]
    (= token (env :admin-token))))

(defn dashboard []
  (layout/render
    "dashboard.html"
    {:users (repo/find-all-users)))

(defroutes admin-routes
  (GET "/admin" [] (restricted (dashboard))))
