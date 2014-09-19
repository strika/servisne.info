(ns servisne-info.controllers.admin
  (:require [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn dashboard []
  (layout/render
    "admin/dashboard.html"
    {:users (repo/find-all-users)
     :news (repo/find-latest-news)}))
