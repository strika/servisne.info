(ns servisne-info.test.tasks.send-notifications
  (:require
    [clojure.test :refer :all]
    [servisne-info.notifications :refer :all]
    [servisne-info.repository :as repo]
    [servisne-info.tasks.send-notifications :refer :all]
    [servisne-info.test-utils :refer :all]))

(use-fixtures :once (compose-fixtures stub-emails init-database))
(use-fixtures :each (compose-fixtures clean-emails clean-database))

(def user {:email "john@example.com"
           :streets ["Bulevar oslobodjenja" "Dunavska"]})
(def power-outage {:title "Power outage"
                   :url "http://example.com/power_outage"
                   :content "Power outage in Dunavska"})
(def closed-road {:title "Closed road"
                  :url "http://example.com/closed_road"
                  :content "Closed road somewhere"})
(def city-wide-water-shortage
  {:title "Slabiji pritisak vode u celom Novom Sadu"
   :url "http://example.com/city_wide_water_shortage"
   :content "Zbog radova Elektrovojvodine na izvorištu Ratno ostrvo, danas će,
            od 7 časova biti snižen pritisak u vodovodnoj mreži u celom gradu
            i prigradskim naseljima."})

(deftest find-news-for-user-test
  (repo/create-user user)
  (doall (map repo/create-news
              [power-outage closed-road city-wide-water-shortage]))
  (let [[result-user result-news] (find-news-for-user user)]
    (is (= result-user user))
    (is (= 2 (count result-news)))
    (is (= (:url (first result-news)) (:url power-outage)))
    (is (= (:url (last result-news)) (:url city-wide-water-shortage)))))

(deftest similar-street-name-test
  (let [user {:email "john@example.com"
              :streets ["Svetozara Miletića" "1300 kaplara"]}]
    (repo/create-user user)
    (repo/create-news {:title "Closed road"
                       :url "http://example.com/closed_road"
                       :content "Closed road in Svetozara Ćorovića."})
    (is (= [user []]
           (find-news-for-user user)))))

(deftest two-words-street-name
  (let [user {:email "john@example.com"
              :streets ["1300 kaplara"]}]
    (repo/create-user user)
    (repo/create-news {:title "Closed road"
                       :url "http://example.com/closed_road"
                       :content "Closed road in 1300 kaplara street."})
    (let [[result-user result-news] (find-news-for-user user)]
      (is (= result-user user))
      (is (not-empty result-news)))))

(deftest send-notifications-test
  (repo/create-user user)
  (repo/create-news power-outage)
  (repo/create-news closed-road)
  (send-notifications)
  (is (= 1 (count @sent-emails)))
  (is (= (:email user) (:to (first @sent-emails))))
  (is (= 0 (count (repo/find-unsent-news))))
  (is (= 1 (count (repo/find-all-events))))
  (assert-event-recorded))
