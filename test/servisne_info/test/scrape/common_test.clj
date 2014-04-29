(ns servisne-info.test.scrape.common-test
  (:use expectations
        servisne-info.scrape.common))

(def info-site
  {:url "http://example.com"
   :links-path "/city/service/"})

(expect "http://example.com/novi-sad/service/"
        (path-to-url info-site "/novi-sad/service/"))

(expect "http://example.com/city/service/"
        (info-links-url info-site))
