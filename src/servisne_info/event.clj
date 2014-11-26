(ns servisne-info.event
  (:require [servisne-info.repository :as repo]))

(defn record
  ([message] (record message {}))
  ([message data]
   (repo/create-event (assoc data :message message))))
