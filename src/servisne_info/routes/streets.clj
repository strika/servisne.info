(ns servisne-info.routes.streets
  (:use compojure.core)
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]
            [servisne-info.views.layout :as layout]))

(defn streets-edit [email]
  (layout/render "streets/edit.html" {:email email}))

(defn streets-update [email streets]
  (mc/update "users" {:email email} {$set {:streets streets}})
  (layout/render "streets/update.html"
                 {:email email :streets streets}))

(defroutes streets-routes
  (GET "/streets/edit" [email] (streets-edit email))
  (POST "/streets/update" [email streets] (streets-update email streets)))
