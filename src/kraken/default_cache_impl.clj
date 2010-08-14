(ns kraken.default-cache-impl
  (require [kraken.cache]))

(deftype memory-cache-impl [map-atom] 
  kraken.cache/kraken-cache-protocol
  (cached-page-impl [instance uri] (@map-atom uri))
  (set-cached-page-impl [instance uri content] 
    (swap! map-atom assoc uri content)
    content)
  (invalidate-cache-entry-impl [instance uri] 
    (swap! map-atom assoc uri nil) 
    nil)
  (clear-cache-impl [instance] (reset! map-atom {})))
