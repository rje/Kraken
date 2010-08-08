(ns kraken.routing
  (:use [aleph.core]
        [kraken.logging]
        [kraken.config])
  (:require [kraken.static-files :as static-files]))

(defn controller-for-uri [uri]
  (let [result (first 
                 (filter 
                   #(not (= nil (re-find (key %) uri))) 
                   @*routes*))]
    (cond (= nil result) nil
          :else (val result))))

(defn have-controller-for-uri? [uri]
 (not (= nil (controller-for-uri uri))))

(defn route [channel request]
  (log :info (str (:remote-addr request) " " 
                  (:request-method request) ": " 
                  (:scheme request) "://"
                  (:server-name request) ":" (:server-port request)
                  (:uri request) 
                  (if (:query-string request) (:query-string request) "")))
  (cond (have-controller-for-uri? (:uri request))
          ((controller-for-uri (:uri request)) channel request)
        (static-files/file-exists? (:uri request))
          (static-files/return-file channel request)
        :else
          (static-files/return-404 channel request)))
