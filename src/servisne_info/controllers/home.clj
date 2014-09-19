(ns servisne-info.controllers.home
  (:require [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn about-page []
  (layout/render "about.html"))

(defn home-page []
  (layout/render
    "home.html"
    {:info-links (repo/find-latest-news)}))

(defn terms-page []
  (layout/render "terms.html"))
