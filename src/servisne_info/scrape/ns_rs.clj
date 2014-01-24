(ns servisne-info.scrape.ns-rs
  (:require [net.cgrand.enlive-html :as en]))

(def info-site
  {:url "http://www.021.rs/"
   :links-path "Novi-Sad/Servisne-informacije/"})

(defn info-links-url [site]
  (str (:url site) (:links-path site)))

(defn info-links-page [site]
  (en/html-resource (java.net.URL. (info-links-url site))))

(defn info-links [html]
  (map
    #(->  % :attrs :href)
    (en/select
      html
      [:div.najava :div.leading :a])))
