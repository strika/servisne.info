(ns servisne-info.scrape.worker
  (:use servisne-info.utils
        [servisne-info.scrape.common :only [html-resource]])
  (:require [servisne-info.repository :as repo]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(defn- new-link? [link]
  (nil? (repo/find-news (:url link))))

(defn- scrape-content [link]
  (let [url (:url link)
        content (ns-scraper/info-page-content (html-resource url))]
    (assoc link :content content)))

(defn- timestamp-link [link]
  (assoc link :created-at (now)))

(defn- save-news [news]
  (doseq [item news]
    (repo/create-news item))
  (count news))

(defn save-links []
  (->> (ns-scraper/links)
       (filter new-link?)
       (map scrape-content)
       (map timestamp-link)
       (save-news)))
