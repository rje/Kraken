(ns kraken.routing
  (:use [aleph.core])
  (:require [kraken.static-files :as static-files]
            [kraken.post-controller :as post-controller]
            [kraken.index-controller :as index-controller]))

(def controller-map {
    #"^/$" index-controller/render-view,
    #"^/post/" post-controller/render-view
  })

(defn controller-for-uri [uri]
  (let [result (first 
                 (filter 
                   #(not (= nil (re-find (key %) uri))) 
                   controller-map))]
    (cond (= nil result) nil
          :else (val result))))

(defn have-controller-for-uri? [uri]
 (not (= nil (controller-for-uri uri))))

(defn route [channel request]
  (cond (have-controller-for-uri? (:uri request))
          ((controller-for-uri (:uri request)) channel request)
        (static-files/file-exists? (:uri request))
          (static-files/return-file channel request)
        :else
          (static-files/return-404 channel request)))
