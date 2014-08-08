(ns servisne-info.handler-utils
  (:require [taoensso.timbre :as timbre]))

(def assets ["/js/" "/css/" "/fonts/" "/images"])

(defn- asset? [request]
  (some #(.startsWith (:uri request) %) assets))

(defn log-request [handler]
  (fn [request]
    (if-not (asset? request)
      (timbre/info "Request start:" (:request-method request) (:uri request) (:query-string request)))
    (handler request)))
