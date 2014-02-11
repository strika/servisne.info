(ns servisne-info.webapp
  (:use org.httpkit.server)
  (:require [servisne-info.handler :as handler]))

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
      (run-server handler/app {:ip ip :port port})))
