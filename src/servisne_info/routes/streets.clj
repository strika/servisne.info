(ns servisne-info.routes.streets
  (:use compojure.core)
  (:require [servisne-info.repository :as repo]
            [servisne-info.views.layout :as layout]))

(defn streets-edit [email]
  (let [user (repo/find-user email)]
    (layout/render "streets/edit.html" user)))

(defn streets-update [email streets]
  (repo/update-user email {:streets streets})
  (layout/render "streets/update.html"
                 {:email email :streets streets}))

(defroutes streets-routes
  (GET "/streets/edit" [email] (streets-edit email))
  (POST "/streets/update" [email streets] (streets-update email streets)))
