(ns kraken.utils
  (:use [aleph.core]))

(def page-cache (atom {}))

(defn cached-page [uri]
  (@page-cache uri))

(defn set-cached-page [uri content]
  (swap! page-cache assoc uri content)
  content)

(defn send-valid-result [channel content]
  (enqueue-and-close channel 
                     {:status 200
                      :headers {"Content-Type" "text/html"}
                      :body content}))
