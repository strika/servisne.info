(defproject servisne-info "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.draines/postal "1.11.2"]
                 [com.novemberain/monger "2.0.0"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/timbre "2.7.1"]
                 [com.taoensso/tower "2.0.1"]
                 [compojure "1.1.6"]
                 [enlive "1.1.5"]
                 [environ "0.4.0"]
                 [http-kit "2.1.16"]
                 [kerodon "0.4.0"]
                 [lib-noir "0.7.9"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [overtone/at-at "1.2.0"]
                 [prone "0.6.0"]
                 [ring-server "0.3.1"]
                 [selmer "0.5.8"]
                 [raven-clj "1.0.0"]]

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
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]]
         :env {:selmer-dev true}}}
  :min-lein-version "2.0.0"
  :aliases {"hello"              ["run" "-m" "servisne-info.tasks.hello-world"]
            "scrape"             ["run" "-m" "servisne-info.tasks.scrape"]
            "send-notifications" ["run" "-m" "servisne-info.tasks.send-notifications"]})
