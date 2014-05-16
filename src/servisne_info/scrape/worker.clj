(ns servisne-info.scrape.worker
  (:use servisne-info.utils)
  (:require [monger.collection :as mc]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(defn save-links []
  (doseq [link (ns-scraper/links)]
    (mc/insert "news" (assoc link :created-at (now)))))
