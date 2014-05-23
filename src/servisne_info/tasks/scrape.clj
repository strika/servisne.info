(ns servisne-info.tasks.scrape
  (:use servisne-info.scrape.worker))

(defn -main [& args]
  (println "Scraped" (save-links) "new links"))
