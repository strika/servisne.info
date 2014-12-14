(ns servisne-info.test-utils
  (:require [clojure.test :refer :all]
            [servisne-info.repository :as repo]
            [servisne-info.handler :refer [init]]
            [servisne-info.notifications :as notifications]))

(def sent-emails (atom []))

(defn stub-send-email [to subject body]
  (swap! sent-emails conj {:to to :subject subject :body body}))

(defn load-cassette [cassette]
  (read-string (slurp (str "test/servisne_info/test/cassettes/" cassette ".clj"))))

(defn stub-emails [t]
  (with-redefs [notifications/send-email stub-send-email]
    (t)))

(defn init-database [t]
  (init)
  (t))

(defn clean-database [t]
  (repo/remove-all)
  (t))

(defn clean-emails [t]
  (reset! sent-emails [])
  (t))

(defn assert-email-sent
  ([session]
   (assert-email-sent session nil))
  ([session {:keys [to subject body]}]
   (let [email (first @sent-emails)]
     (is (= 1 (count @sent-emails)))
     (if to
       (is (= to (:to email))))
     (if subject
       (is (= subject (:subject email))))
     (if body
       (is (.contains (:body email) body))))))

(defn assert-event-recorded
  ([] (is (= 1 (count (repo/find-all-events)))))
  ([session]
   (assert-event-recorded)
   session))
