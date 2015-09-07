(ns servisne-info.validations)

(defn user-valid? [user]
  (not (nil? (:email user))))
