(ns servisne-info.tasks.scrape
  (:use servisne-info.scrape.worker)
  (:require [taoensso.timbre :as timbre]))

(defn -main [& args]
  (timbre/info "Scrape start")
  (save-links)
  (timbre/info "Scrape end"))
