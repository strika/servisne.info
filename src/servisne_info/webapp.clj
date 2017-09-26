(ns servisne-info.webapp
  (:use org.httpkit.server)
  (:require [environ.core :refer [env]]
            [servisne-info.handler :as handler]))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (handler/destroy)
    (reset! server nil)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (reset! server (run-server handler/app {:port port}))
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
    (handler/init)))
