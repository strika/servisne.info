(ns servisne-info.routes.admin
  (:use compojure.core)
  (:require [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn dashboard []
  (layout/render
    "dashboard.html"))

(defroutes admin-routes
  (GET "/" [] (dashboard)))
