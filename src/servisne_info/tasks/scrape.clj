(ns servisne-info.tasks.scrape
  (:use servisne-info.scrape.worker)
  (:require [servisne-info.persistence :refer [db-connect db-disconnect]]))

(defn -main [& args]
  (db-connect)
  (println "Scraped" (save-links) "new links")
  (db-disconnect))
