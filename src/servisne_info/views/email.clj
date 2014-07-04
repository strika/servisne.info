(ns servisne-info.views.email
  (:require [selmer.parser :as parser]))

(def template-path "servisne_info/views/emails/")

(defn render [template & [params]]
  (parser/render-file (str template-path template) params))
