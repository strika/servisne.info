(ns servisne-info.scrape.ns-rs
  (:use servisne-info.scrape.common)
  (:require [net.cgrand.enlive-html :as en]
            [clojure.string :as s]))

; Site description

(def info-site
  {:url "http://www.021.rs"
   :links-path "/Novi-Sad/Servisne-informacije"})

; Private

(defn- info-page-title [html]
  (s/trim (first (:content (first (en/select html [:h1.contentheading]))))))

; Public

(defn info-page-content [html]
  (s/join ""
          (map s/trim
               (en/select html [:div#content en/text-node]))))

(defn info-page [html]
  {:title (info-page-title html)
   :content (info-page-content html)})

(defn info-links [site html]
  (map
    (fn [a]
      {:title (s/trim (first (:content a)))
       :url (path-to-url site (:href (:attrs a)))})
    (en/select
      html
      [:div.najava :div.leading :h2.contentheading :a])))

(defn links []
  (info-links info-site (info-links-page info-site)))
