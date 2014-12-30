(ns servisne-info.test-utils
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :refer :all]
            [servisne-info.repository :as repo]
            [servisne-info.handler :refer [init]]
            [servisne-info.notifications :as notifications]
            [servisne-info.webapp :as app]))

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

(def ^:private browser-count (atom 0))

(defn browser-up
  "Start up a browser if it's not already started."
  []
  (when (= 1 (swap! browser-count inc))
    (set-driver! {:browser :firefox})
    (implicit-wait 60000)))

(defn browser-down
  "If this is the last request, shut the browser down."
  [& {:keys [force] :or {force false}}]
  (when (zero? (swap! browser-count (if force (constantly 0) dec)))
    (quit)))

(defn start-server [f]
  (app/start-server "0.0.0.0" 8181)
  (f)
  (app/stop-server))
