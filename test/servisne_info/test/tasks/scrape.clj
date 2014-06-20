(ns servisne-info.test.tasks.scrape
  (:use clojure.test
        servisne-info.features.common
        servisne-info.test-utils
        servisne-info.tasks.scrape)
  (:require [monger.collection :as mc]
            [servisne-info.scrape.common :as scrape-common]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(use-fixtures :once fixtures-once)
(use-fixtures :each fixtures-each)

(def info-page-fixture (load-cassette "info_page"))

(deftest test-save-links
  (let [power-outage {:title "Power outage"
                      :url "http://example.com/power_outage"}]
    (with-redefs [ns-scraper/links (constantly [power-outage])
                  scrape-common/html-resource (constantly info-page-fixture)]
      (let [links-count (save-links)]

        (testing "it returns links count"
          (is (= 1 links-count)))

        (testing "it saves links to the database"
          (let [news (mc/find-maps "news")
                latest (first news)]
            (is (= (:title latest) (:title power-outage)))
            (is (= (:url latest) (:url power-outage)))
            (is (.startsWith (:content latest) "Novoizgrađeni deo Detelinarske pijace"))))

        (testing "when the link was already saved to the database"
          (save-links)
          (let [news (mc/find-maps "news")]
            (is (= (count news) 1))))))))
