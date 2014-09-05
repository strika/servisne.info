(ns servisne-info.logging
  (:require [com.postspectacular.rotor :as rotor]
            [taoensso.timbre :as timbre]))

(defn setup []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/append})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "servisne_info.log" :max-size (* 512 1024) :backlog 10}))

(defn format-data [data]
  (apply str
         (map
           (fn [[k v]] (str " " (name k) "='" v "'"))
           data)))

(defn format-message
  ([message] message)
  ([message data]
   (str message "," (format-data data))))

(defn info
  ([message] (info message {}))
  ([message data]
   (timbre/info (format-message message data))))
