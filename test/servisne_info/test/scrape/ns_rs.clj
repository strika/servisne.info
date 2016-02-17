(ns servisne-info.test.scrape.ns-rs
  (:use clojure.test
        servisne-info.test-utils
        servisne-info.scrape.ns-rs))

(def links-page-fixture (load-cassette "links_page"))
(def info-page-fixture (load-cassette "info_page"))

(deftest info-links-test
  (let [links (info-links links-page-fixture)]
    (is (= (count links) 15))
    (is (= (first links)
           {:title "Deo Futoškog puta i Subotičke ulice  u ponedeljak bez vode"
            :url "http://www.021.rs/story/6/126530/Deo-Futokog-puta-i-Subotike-ulice-u-ponedeljak-bez-vode.html"}))
    (is (= (last links)
           {:title "Vanredni polasci autobusa GSP-a za doček Srpske Nove godine"
            :url "http://www.021.rs/story/6/126291/Vanredni-polasci-autobusa-GSP-a-za-doek-Srpske-Nove-godine.html"}))))

(deftest info-page-test
  (let [page (info-page info-page-fixture)]
    (is (= (:title page) "Isključenja struje za 11. januar"))
    (is (.startsWith (:content page) "Isključenja struje za 11. januar"))))

(deftest info-page-content-test
  (let [content (info-page-content info-page-fixture)]
    (is (.startsWith content "Isključenja struje za 11. januar"))))
