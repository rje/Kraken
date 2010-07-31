(ns kraken.routing
  (:require [aleph.core :as alephc]
            [kraken.static-files :as static-files]))

(defn return-placeholder-index [channel request]
  (alephc/enqueue-and-close channel
    {:status 200
     :body "This is a default index, have a link to a <a href=\"foo.html\">static page</a>"}))

(defn route [channel request]
  (cond (= "/" (:uri request))
          (return-placeholder-index channel request)
        (static-files/file-exists? (:uri request))
          (static-files/return-file channel request)
        :else
          (static-files/return-404 channel request)))
