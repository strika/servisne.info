(ns servisne-info.test.scrape.common-test
  (:use clojure.test
        servisne-info.scrape.common))

(def info-site
  {:url "http://example.com"
   :links-path "/city/service/"})

(deftest path-to-url-test
  (is (= (path-to-url info-site "/novi-sad/service/")
         "http://example.com/novi-sad/service/")))

(deftest info-links-url-test
  (is (= (info-links-url info-site)
         "http://example.com/city/service/")))
