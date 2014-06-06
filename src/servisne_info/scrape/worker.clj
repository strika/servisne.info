(ns servisne-info.scrape.worker
  (:use servisne-info.utils)
  (:require [monger.collection :as mc]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(defn- new-link? [link]
  (nil? (mc/find-one "news" {:url (:url link)})))

(defn- select-new-links [links]
  (filter new-link? links))

(defn save-links []
  (let [links (ns-scraper/links)]
    (doseq [link (select-new-links links)]
      (mc/insert "news" (assoc link :created-at (now))))
    (count links)))
