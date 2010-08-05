(ns kraken.index-controller
  (:import [java.io File]
           [java.util Date]
           [java.text SimpleDateFormat])
  (:use [aleph.core]
        [kraken.utils]
        [clojure.contrib.duck-streams :only [pwd]])
  (:require [net.cgrand.enlive-html :as html]
            [com.ashafa.clutch :as couchdb]
            [kraken.index-controller :as index-controller]))

(def *blog-database*
  (couchdb/get-database {:name "blog"
                 :language "clojure"}))

(defn date-to-string [#^String date-string]
  (let [formatter (SimpleDateFormat. "yyyy/MM/dd")
        date (.parse formatter date-string)
        output-formatter (SimpleDateFormat. "MMM dd, yyyy")]
    (.format output-formatter date)))

(html/defsnippet 
  post-template 
  (File. (str (pwd) "/templates/index.html"))
  [:div.post]
  [post-entry]
  [:h3.posttitle :a] (html/content (:title (:value post-entry)))
  [:h3.posttitle :a] 
    (html/set-attr :href (str "/post/" (:title (:value post-entry))))
  [:.postdate] (html/content (date-to-string (:date (:value post-entry))))
  [:.postbody] (html/html-content (:body (:value post-entry))))

(html/deftemplate 
  index-template  
  (File. (str (pwd) "/templates/index.html"))
  [all-posts]
  [:div#posts] (html/content (map post-template all-posts)))

(defn render-view [channel request]
  (if (not= nil (cached-page (:uri request)))
    (send-valid-result channel (cached-page (:uri request)))
    (couchdb/with-db *blog-database*
      (let [posts (couchdb/get-view "posts" :all-posts 
                    {:descending true, :limit 10})]
        (send-valid-result 
          channel
          (set-cached-page 
            (:uri request) 
            (apply str (index-template (:rows posts)))))))))
