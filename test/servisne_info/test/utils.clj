(ns servisne-info.test.utils
  (:use clojure.test
        servisne-info.utils))

(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))

(deftest str-partition-test
  (let [text "abcdefgh"]
    (is (= (str-partition text 3)
           ["abc" "bcd" "cde" "def" "efg" "fgh"]))))

(deftest fuzzy-has-text-test
  (let [text "U toku je popravka havarije, a bez tople vode su:
             Bulevar kneza Miloša 11-37;
             Mileve Marić 1-37, 2-68;
             Momčila Tapavice 2-36;
             Stanoja Stanojevića 3-15, 2-20;
             Bulevar Jovana Dučića 39-47."]
    (is (fuzzy-has-text? "Bulevar Jovana DuČića" text))
    (is (fuzzy-has-text? "jovana ducica" text))
    (is (fuzzy-has-text? "Mileve Maric" text))
    (is (not (fuzzy-has-text? "Bulevar Oslobodjenja" text)))
    (is (not (fuzzy-has-text? "1300 kaplara" text)))))
