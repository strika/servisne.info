(ns servisne-info.webapp
  (:use org.httpkit.server)
  (:require [servisne-info.handler :as handler]))

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (handler/destroy)
    (reset! server nil)))

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
    (reset! server (run-server handler/app {:ip ip :port port}))
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
    (handler/init)))
