(ns servisne-info.tasks.scrape
  (:use servisne-info.scrape.worker))

(defn -main [& args]
  (println "Scrape start")
  (save-links)
  (println "Scrape end"))
