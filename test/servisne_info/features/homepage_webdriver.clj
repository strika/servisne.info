(ns servisne-info.features.homepage-webdriver
  (:use clojure.test
        clj-webdriver.taxi
        servisne-info.utils
        servisne-info.test-utils)
  (:require [clj-time.core :as t]
            [servisne-info.repository :as repo]
            [servisne-info.handler :refer [app]]))

(use-fixtures :once start-server init-database)
(use-fixtures :each clean-database)

(deftest homepage
  (browser-up)
  (let [power-outage {:title "Isključenja struje za 20. januar"
                      :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"
                      :created-at (t/now)}]
    (repo/create-news power-outage)
    (to "0.0.0.0:8181")
    (is (= "Budite spremni za nestanak struje,  \n    vode ili grejanja" (text "h2")))
    (is (= (:title power-outage) (text "div.latest-info li :"))))
  (browser-down))
