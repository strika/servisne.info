(ns servisne-info.scrape.common
  (:require [net.cgrand.enlive-html :as en]
            [clojure.string :as s]))

(defn html-resource
  "Returns html resource for the url"
  [url]
  (en/html-resource (java.net.URL. url)))

(defn path-to-url
  "Transforms a path to the url for the site"
  [site path]
  (str (:url site) path))

(defn info-links-url
  "Returns info links url for the site"
  [site]
  (str (:url site) (:links-path site)))

(defn info-links-page
  "Returns info links html resource for the site"
  [site]
  (html-resource (info-links-url site)))
