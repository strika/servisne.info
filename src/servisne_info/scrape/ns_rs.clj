(ns servisne-info.scrape.ns-rs
  (:use servisne-info.scrape.common)
  (:require [net.cgrand.enlive-html :as en]
            [clojure.string :as s]))

; Site description

(def info-site
  {:url "http://www.021.rs"
   :links-path "/menu/index/6"})

; Public

(defn info-page-title [html]
  (s/trim (first (:content (first (en/select html [:.story :h1]))))))

(defn info-page-content [html]
  (s/join ""
          (map s/trim
               (en/select html [:div.story en/text-node]))))

(defn info-page [html]
  {:title (info-page-title html)
   :content (info-page-content html)})

(defn info-links [site html]
  (map
    (fn [a]
      {:title (s/trim (first (:content (first (en/select a [:span])))))
       :url (:href (:attrs a))})
    (en/select
      html
      [:.col-md-8 :.article_title :a])))

(defn links []
  (info-links info-site (info-links-page info-site)))
