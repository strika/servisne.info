(ns servisne-info.routes.home
  (:use compojure.core)
  (:require [servisne-info.views.layout :as layout]
            [servisne-info.scrape.ns-rs :as scrape]))

(defn home-page []
  (let [info-links (scrape/links)]
    (layout/render
      "home.html"
      {:info-links info-links})))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
