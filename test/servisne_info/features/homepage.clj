(ns servisne-info.features.homepage
  (:use kerodon.core
        kerodon.test
        clojure.test
        servisne-info.utils
        servisne-info.test-utils)
  (:require [servisne-info.repository :as repo]
            [servisne-info.handler :refer [app]]))

(use-fixtures :once init-database)
(use-fixtures :each clean-database)

(deftest homepage
  (let [power-outage {:title "Isključenja struje za 20. januar"
                      :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"
                      :created-at (now)}]
    (repo/create-news power-outage)
    (-> (session app)
        (visit "/")
        (within [:h2]
          (has (text? "Budite spremni za nestanak struje,  \n    vode ili grejanja")))
        (within [:div.latest-info :li]
          (has (text? (:title power-outage)))))))
