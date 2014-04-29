(ns servisne-info.test.handler
  (:use expectations
        ring.mock.request
        servisne-info.handler))

(expect 200 (:status (app (request :get "/"))))

(expect 404 (:status (app (request :get "/invalid"))))
