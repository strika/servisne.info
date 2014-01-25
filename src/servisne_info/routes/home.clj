(ns servisne-info.routes.home
  (:use compojure.core)
  (:require [servisne-info.views.layout :as layout]
            [servisne-info.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content "Hello World"}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
