(ns servisne-info.tasks.hello-world
  (:require [taoensso.timbre :as timbre]))

(defn -main [& args]
  (timbre/info "Hello World!")
  (println "Hello World!"))
