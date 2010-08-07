(ns kraken.config)

(def *port* (ref 8080))
(def *routes* (atom {}))

(defn set-port [new-port]
  (dosync (ref-set *port* new-port)))

(defn set-routes [new-routes]
  (reset! *routes* new-routes))

