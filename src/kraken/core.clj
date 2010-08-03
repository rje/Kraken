(ns kraken.core
  (:gen-class)
  (:use [aleph.core])
  (:require [aleph.http]
            [kraken.routing :as routing]))

(defn -main [& args]
  (aleph.http/start-http-server routing/route {:port 8080}))
