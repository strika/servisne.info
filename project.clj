(defproject servisne-info "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-aws-s3 "0.3.10"]
                 [clj-time "0.9.0"]
                 [com.draines/postal "1.11.3"]
                 [com.novemberain/monger "2.1.0"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/timbre "3.3.1"]
                 [compojure "1.3.1"]
                 [enlive "1.1.5"]
                 [environ "1.0.0"]
                 [http-kit "2.1.19"]
                 [lib-noir "0.9.5"]
                 [org.clojure/tools.nrepl "0.2.6"]
                 [overtone/at-at "1.2.0"]
                 [prone "0.8.0"]
                 [raven-clj "1.2.0"]
                 [ring-server "0.3.1"]
                 [selmer "0.7.8"]]
  :repl-options {:init-ns servisne-info.repl}
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]]
  :ring {:handler servisne-info.handler/app
         :init    servisne-info.handler/init
         :destroy servisne-info.handler/destroy}
  :main servisne-info.webapp
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[kerodon "0.5.0"]
                        [ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]]
         :env {:selmer-dev true}}}
  :min-lein-version "2.0.0"
  :aliases {"hello"              ["run" "-m" "servisne-info.tasks.hello-world"]
            "scrape"             ["run" "-m" "servisne-info.tasks.scrape"]
            "send-notifications" ["run" "-m" "servisne-info.tasks.send-notifications"]})
