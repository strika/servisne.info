(ns servisne-info.utils
  (:require [environ.core :refer [env]]))

(defn production? []
  (env :openshift-app-name))
