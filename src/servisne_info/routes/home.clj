(ns servisne-info.routes.home
  (:use compojure.core)
  (:require [servisne-info.scrape.ns-rs :as scrape]
            [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn home-page []
  (layout/render
    "home.html"
    {:info-links (repo/find-latest-news)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
