(ns kraken.core
  (:gen-class)
  (:use [kraken.config])
  (:require [aleph.http]
            [kraken.routing :as routing]))

(defn start-server []
  (aleph.http/start-http-server routing/route {:port @*port*}))
