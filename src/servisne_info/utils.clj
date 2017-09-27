(ns servisne-info.utils
  (:require [environ.core :refer [env]]))

(defn production? []
  (== (env :app-environment) "production"))
