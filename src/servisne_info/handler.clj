(ns servisne-info.handler  
  (:use [raven-clj.ring :only [wrap-sentry]]
        [servisne-info.handler-utils :only [log-request]]
        [servisne-info.tasks :refer [schedule-periodic-tasks]])
  (:require [compojure.core :refer [defroutes]]            
            [compojure.route :as route]
            [environ.core :refer [env]]
            [noir.util.middleware :as middleware]
            [selmer.parser :as parser]
            [servisne-info.logging :as l]
            [servisne-info.repository :refer [db-connect db-disconnect]]
            [servisne-info.routes.admin :refer [admin-routes admin-access]]
            [servisne-info.routes.home :refer [home-routes]]
            [servisne-info.routes.users :refer [users-routes]]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (l/setup)
  (db-connect)
  (schedule-periodic-tasks)
  (if (env :selmer-dev) (parser/cache-off!))
  (l/info "servisne-info started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (db-disconnect)
  (l/info "servisne-info is shutting down..."))

(defn capture-exceptions [handler]
  (let [sentry-dsn (env :sentry-dsn)]
    (if sentry-dsn
      (wrap-sentry handler sentry-dsn)
      handler)))

(defn template-error-page [handler]
  (if (env :selmer-dev)
    (fn [request]
      (try
        (handler request)
        (catch clojure.lang.ExceptionInfo ex
          (let [{:keys [type error-template] :as data} (ex-data ex)]
            (if (= :selmer-validation-error type)
              {:status 500
               :body (parser/render error-template data)}
              (throw ex))))))
    handler))

(def app (middleware/app-handler
           ;; add your application routes here
           [admin-routes home-routes users-routes app-routes]
           ;; add custom middleware here
           :middleware [template-error-page capture-exceptions log-request]
           ;; add access rules here
           :access-rules [admin-access]
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
