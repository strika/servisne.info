(ns servisne-info.test.scrape.worker-test
  (:use clojure.test
        servisne-info.scrape.worker
        servisne-info.features.common)
  (:require [monger.collection :as mc]
            [servisne-info.scrape.ns-rs :as ns-scraper]))

(use-fixtures :once fixtures-once)
(use-fixtures :each fixtures-each)

(deftest test-save-links
  (let [power-outage {:title "Power outage"
                      :url "http://example.com/power_outage"}]
    (with-redefs [ns-scraper/links (constantly [power-outage])]
      (let [links-count (save-links)]

        (testing "it returns links count"
          (is (= 1 links-count)))

        (testing "it saves links to the database"
          (let [news (mc/find-maps "news")
                latest (first news)]
            (is (= (:title latest) (:title power-outage)))
            (is (= (:url latest) (:url power-outage)))))

        (testing "when the link was already saved to the database"
          (save-links)
          (let [news (mc/find-maps "news")]
            (is (= (count news) 1))))))))
