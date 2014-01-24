(ns servisne-info.test.utils)

(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))
