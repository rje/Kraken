(ns kraken.utils
  (:use [aleph.core]
        [kraken.logging])
  (:import [java.net URLDecoder]
           [org.jboss.netty.buffer ChannelBufferInputStream]))

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

;; Helpers for request->query-map (see below)
(defn- query-map-from-param-string [param-string]
  (if (or (= param-string nil) (= param-string ""))
    {}
    (let [decoded-string (URLDecoder/decode param-string)]
      (apply hash-map (-> decoded-string (.split "[&=]"))))))

(defn- body->string [request]
  (if (not= nil (:body request))
    (let [byte-array (make-array (. Byte TYPE) (:content-length request))
          ^ChannelBufferInputStream body (:body request)]
      (.readFully body byte-array)
      (.trim (String. byte-array)))
    ""))

(defn- query-map-from-post-body [request]
  (let [param-string (body->string request)]
    (query-map-from-param-string param-string)))

(defn- query-map-from-any [request]
  (cond
    (not= nil (:query-string request))
      (query-map-from-param-string (:query-string request))
    (not= nil (:body request))
      (query-map-from-post-body request)
    :else
      {}))

;; Create a map of the query params.  It will use the :request-method value
;; to determine where to pull parameters from.  In the case of no params,
;; it will return an empty map
(defn request->query-map [request]
  (cond
    (= :get (:request-method request))
      (query-map-from-param-string (:query-string request))
    (= :post (:request-method request))
      (query-map-from-post-body request)
    :else
      (query-map-from-any request)))
