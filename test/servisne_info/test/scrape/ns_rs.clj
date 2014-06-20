(ns servisne-info.test.scrape.ns-rs
  (:use clojure.test
        servisne-info.test.utils
        servisne-info.scrape.ns-rs))

(def links-page-fixture (load-cassette "links_page"))
(def info-page-fixture (load-cassette "info_page"))

(deftest info-links-test
  (let [links (info-links info-site links-page-fixture)]
    (is (= (count links) 10))
    (is (= (first links)
           {:title "Isključenja struje za 20. januar"
            :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"}))
    (is (= (last links)
           {:title "Od srede ponovo đački polasci"
            :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Od-srede-ponovo-dacki-polasci.html"}))))

(deftest info-page-test
  (let [page (info-page info-page-fixture)]
    (is (= (:title page) "Proširena Detelinarska pijaca"))
    (is (.startsWith (:content page) "Novoizgrađeni deo Detelinarske pijace"))))

(deftest info-page-content-test
  (let [content (info-page-content info-page-fixture)]
    (is (.startsWith content "Novoizgrađeni deo Detelinarske pijace"))))
