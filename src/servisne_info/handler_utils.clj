(ns servisne-info.handler-utils
  (:require [servisne-info.logging :as l]))

(def assets ["/js/" "/css/" "/fonts/" "/images"])

(defn- asset? [request]
  (some #(.startsWith (:uri request) %) assets))

(defn log-request [handler]
  (fn [request]
    (if-not (asset? request)
      (l/info "Request start" {:method (:request-method request) :uri (:uri request) :query (:query-string request)}))
    (handler request)))
