(ns servisne-info.test.utils
  (:use expectations)
  (:require [net.cgrand.enlive-html :as en]))

(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))

(defn expect-on-page
  [response selector expected]
  (expect expected (first (en/select (:enlive response) selector)))
  response)
