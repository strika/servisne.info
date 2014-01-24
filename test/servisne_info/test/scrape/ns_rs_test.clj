(ns servisne-info.test.scrape.ns-rs-test
  (:use clojure.test
        servisne-info.scrape.ns-rs))

(def html-fixture
  (read-string (slurp "test/servisne_info/test/cassettes/links_page.clj")))

(deftest info-links-url-test
  (is (= (info-links-url info-site) "http://www.021.rs/Novi-Sad/Servisne-informacije/")))

(deftest info-links-test
  (let [links (info-links html-fixture)]
    (is (= (count links) 30))
    (is (= (first links) "/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"))
    (is (= (last links) "/Novi-Sad/Servisne-informacije/Od-srede-ponovo-dacki-polasci.html"))))
