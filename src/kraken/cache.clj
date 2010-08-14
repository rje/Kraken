(ns kraken.cache)

(defprotocol kraken-cache-protocol
  (cached-page-impl [instance uri])
  (set-cached-page-impl [instance uri content])
  (invalidate-cache-entry-impl [instance uri])
  (clear-cache-impl [instance]))

;; By default we don't set a cache impl -- set one in the config for now
(def *active-cache* (ref nil))

(defn set-cache-system [cache-system]
  (dosync (ref-set *active-cache* cache-system)))

(defn cached-page [uri] 
  (cached-page-impl @*active-cache* uri))

(defn set-cached-page [uri content]
  (set-cached-page-impl @*active-cache* uri content))

(defn invalidate-cache-entry [uri]
  (invalidate-cache-entry-impl @*active-cache* uri))

(defn clear-cache []
  (clear-cache-impl @*active-cache*))
