(ns servisne-info.test.scrape.ns-rs
  (:use expectations
        servisne-info.test-utils
        servisne-info.scrape.ns-rs))

(def links-page-fixture (load-cassette "links_page"))
(def info-page-fixture (load-cassette "info_page"))

(let [links (info-links info-site links-page-fixture)]
  (expect 10 (count links))
  (expect {:title "Isključenja struje za 20. januar"
           :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Iskljucenja-struje-za-20-januar-2.html"}
          (first links))
  (expect {:title "Od srede ponovo đački polasci"
           :url "http://www.021.rs/Novi-Sad/Servisne-informacije/Od-srede-ponovo-dacki-polasci.html"}
          (last links)))

(let [page (info-page info-page-fixture)]
  (expect "Proširena Detelinarska pijaca" (:title page))
  (expect #"\ANovoizgrađeni deo Detelinarske pijace" (:content page)))
