(ns servisne-info.scrape.ns-rs
  (:require [net.cgrand.enlive-html :as en]))

(defn info-links-page []
  (en/html-resource (java.net.URL. "http://www.021.rs/Novi-Sad/Servisne-informacije/")))

(defn info-links []
  (map
    #(->  % :attrs :href)
    (en/select
      (info-links-page)
      [:div.najava :div.leading :a])))
