(ns servisne-info.features.common
  (:use clojure.test)
  (:require [monger.collection :as mc]
            [servisne-info.handler :refer [init]]))

(defn fixtures-once [t]
  (init)
  (t))

(defn fixtures-each [t]
  (fn [t]
    (mc/remove "users")
    (t)))