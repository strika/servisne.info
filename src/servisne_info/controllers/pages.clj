(ns servisne-info.controllers.pages
  (:require [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn about-page []
  (layout/render "pages/about.html"))

(defn contact-page []
  (layout/render "pages/contact.html"))

(defn home-page []
  (layout/render
    "pages/home.html"
    {:info-links (repo/find-latest-news)}))

(defn terms-page []
  (layout/render "pages/terms.html"))
