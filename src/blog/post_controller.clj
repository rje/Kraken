(ns blog.post-controller
  (:import [java.io File]
           [java.net URLDecoder])
  (:use [aleph.core]
        [kraken.utils]
        [blog.posts]
        [clojure.contrib.string :only [trim]]
        [clojure.contrib.duck-streams :only [pwd]])
  (:require [net.cgrand.enlive-html :as html]))

(defn key-from-uri [uri]
  (trim (nth (.split (URLDecoder/decode uri) "/") 2)))

(html/deftemplate 
  post-template  
  (File. (str (pwd) "/templates/index.html"))
  [post-data]
  [:.posttitle] (html/content (:title post-data))
  [:.postdate] (html/content (:date post-data))
  [:.postbody] (html/html-content (:body post-data)))

(defn render-view [channel request]
  (let [uri (:uri request)
        cached-entry (cached-page uri)]
    (if (not= nil cached-entry)
      (send-valid-result channel cached-entry)
        (let [post-data (get-post-with-title (key-from-uri uri))]
          (send-valid-result
            channel
            (set-cached-page uri (apply str (post-template post-data))))))))
