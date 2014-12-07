(ns servisne-info.tasks.scrape
  (:use servisne-info.utils
        [servisne-info.tasks.task-definition :only [deftask]]
        [servisne-info.scrape.common :only [html-resource]])
  (:require [servisne-info.event :as event]
            [servisne-info.repository :as repo]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

; Private

(defn- new-link? [link]
  (nil? (repo/find-news (:url link))))

(defn- scrape-content [link]
  (let [url (:url link)
        content (ns-scraper/info-page-content (html-resource url))]
    (assoc link :content content)))

(defn- save-news [news]
  (doseq [item news]
    (repo/create-news item))
  (count news))

; Public

(defn save-links []
  (->> (ns-scraper/links)
       (filter new-link?)
       (map scrape-content)
       (save-news)))

(def scrape-task
  (deftask "scrape"
    (let [saved-links-count (save-links)]
      (if (> saved-links-count 0)
        (event/record "Scraping new links" {:count saved-links-count})))))
