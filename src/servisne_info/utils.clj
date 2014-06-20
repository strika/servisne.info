(ns servisne-info.utils
  (:use fuzzy-string.core))

(defn now []
  (java.util.Date.))

(defn str-partition [s n]
  (map 
    #(apply str (take n (drop % s)))
    (range 0 (inc (- (count s) n)))))

(defn fuzzy-has-text? [s text]
  (let [partitioned-text (str-partition text (count s))]
    (not-every? #(> % 5)
                (map #(levenshtein s %) partitioned-text))))
