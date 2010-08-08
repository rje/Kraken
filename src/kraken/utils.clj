(ns kraken.utils
  (:use [aleph.core]
        [kraken.logging]))

;; Functions for having a simple in-memory caching system
(def page-cache (atom {}))

(defn cached-page [uri]
  (@page-cache uri))

(defn set-cached-page [uri content]
  (swap! page-cache assoc uri content)
  content)

(defn invalidate-cache-entry [uri]
  (set-cached-page uri nil))

(defn clear-cache []
  (reset! page-cache {}))

;; A simple wrapper to return an http response
(defn send-valid-result [channel content]
  (enqueue-and-close channel 
                     {:status 200
                      :headers {"Content-Type" "text/html"}
                      :body content}))
