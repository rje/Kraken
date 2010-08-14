(ns kraken.config
  (:require [kraken.logging]
            [kraken.cache]))

(def *port* (ref 8080))
(def *routes* (atom {}))

(defn set-port [new-port]
  (dosync (ref-set *port* new-port)))

(defn set-routes [new-routes]
  (reset! *routes* new-routes))

(defn set-log-file [log-file]
  (kraken.logging/set-logging-file log-file))

(defn set-log-level
  "See kraken.logging/set-logging-level for options"
  [log-level]
  (kraken.logging/set-logging-level log-level))

(defn set-cache-implementation
  "Choose an implementation for page caching.  Right now
   kraken.default-cache.memory-cache-impl is provided"
  [impl]
  (kraken.cache/set-cache-system impl))
