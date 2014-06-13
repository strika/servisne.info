(ns servisne-info.scrape.worker
  (:use servisne-info.utils)
  (:require [servisne-info.repository :as repo]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(defn- new-link? [link]
  (nil? (repo/find-news (:url link))))

(defn- timestamp-link [link]
  (assoc link :created-at (now)))

(defn- save-news [news]
  (doseq [item news]
    (repo/create-news item))
  (count news))

(defn save-links []
  (->> (ns-scraper/links)
       (filter new-link?)
       (map timestamp-link)
       (save-news)))
