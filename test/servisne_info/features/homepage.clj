(ns servisne-info.features.homepage
  (:use kerodon.core
        kerodon.test
        clojure.test
        servisne-info.utils
        servisne-info.features.common)
  (:require [monger.collection :as mc]
            [servisne-info.handler :refer [app]]))

(use-fixtures :once fixtures-once)
(use-fixtures :each fixtures-each)

(deftest homepage
  (let [link {:title "Isključenja struje za 20. januar"
              :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"
              :created-at (now)}]
    (mc/insert "news" link)
    (-> (session app)
        (visit "/")
        (within [:div.jumbotron :h2]
          (has (text? "Dobrodošli na servisne.info")))
        (within [:div.latest-info :li]
          (has (text? (:title link)))))))
