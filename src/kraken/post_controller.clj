(ns kraken.post-controller
  (:import [java.io File]
           [java.net URLDecoder]
           [java.util Date]
           [java.text SimpleDateFormat])
  (:use [aleph.core]
        [kraken.utils]
        [clojure.contrib.string :only [trim]]
        [clojure.contrib.duck-streams :only [pwd]])
  (:require [net.cgrand.enlive-html :as html]
            [com.ashafa.clutch :as couchdb]
            [kraken.index-controller :as index-controller]))

(def *blog-database*
  (couchdb/get-database {:name "blog"
                 :language "clojure"}))

(defn key-from-uri [uri]
  (trim (nth (.split uri "/") 2)))

(html/deftemplate 
  post-template  
  (File. (str (pwd) "/templates/index.html"))
  [post-data]
  [:.posttitle] (html/content (:title post-data))
  [:.postdate] (html/content (:date post-data))
  [:.postbody] (html/html-content (:body post-data)))

(defn render-view [channel request]
  (if (not= nil (cached-page (:uri request)))
    (send-valid-result channel (cached-page (:uri request)))
    (couchdb/with-db *blog-database*
     (let [post-key (.trim (key-from-uri (URLDecoder/decode (:uri request))))
           posts (couchdb/get-view "posts" :posts-with-title {:key post-key})
           post-data (:value (first (:rows posts)))]
       (send-valid-result
         channel
         (set-cached-page 
           (:uri request)
           (apply str (post-template post-data))))))))
