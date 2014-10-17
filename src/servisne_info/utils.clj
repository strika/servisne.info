(ns servisne-info.utils
  (:require [environ.core :refer [env]]))

(defn now []
  (java.util.Date.))

(defn production? []
  (env :openshift-app-name))
