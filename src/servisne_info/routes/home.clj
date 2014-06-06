(ns servisne-info.routes.home
  (:use compojure.core)
  (:require [monger.query :as mq]
            [servisne-info.views.layout :as layout]
            [servisne-info.scrape.ns-rs :as scrape]))

(defn- latest-news []
  (mq/with-collection "news"
    (mq/find {})
    (mq/fields [:url :title :created-at])
    (mq/sort (array-map :created-at 1))
    (mq/limit 10)))

(defn home-page []
  (layout/render
    "home.html"
    {:info-links (latest-news)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
