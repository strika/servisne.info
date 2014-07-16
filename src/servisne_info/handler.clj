(ns servisne-info.handler  
  (:use [raven-clj.ring :only [wrap-sentry]])
  (:require [com.postspectacular.rotor :as rotor]
            [compojure.core :refer [defroutes]]            
            [compojure.route :as route]
            [environ.core :refer [env]]
            [noir.util.middleware :as middleware]
            [selmer.parser :as parser]
            [servisne-info.repository :refer [db-connect db-disconnect]]
            [servisne-info.routes.home :refer [home-routes]]
            [servisne-info.routes.users :refer [users-routes]]
            [taoensso.timbre :as timbre]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/append})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "servisne_info.log" :max-size (* 512 1024) :backlog 10})

  (db-connect)

  (if (env :selmer-dev) (parser/cache-off!))
  (timbre/info "servisne-info started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (db-disconnect)
  (timbre/info "servisne-info is shutting down..."))

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
           [home-routes users-routes app-routes]
           ;; add custom middleware here
           :middleware [template-error-page capture-exceptions]
           ;; add access rules here
           :access-rules []
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
