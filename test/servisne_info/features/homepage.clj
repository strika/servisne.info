(ns servisne-info.features.homepage
  (:use kerodon.core
        kerodon.test
        clojure.test)
  (:require [servisne-info.handler :refer [app]]))


(deftest homepage
  (-> (session app)
      (visit "/")
      (within [:div.jumbotron :h1]
        (has (text? "Dobrodošli na servisne.info")))))
