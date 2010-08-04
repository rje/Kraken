(ns kraken.index-controller
  (:import [java.io File])
  (:use [aleph.core]
        [clojure.contrib.duck-streams :only [pwd]])
  (:require [net.cgrand.enlive-html :as html]
            [kraken.index-controller :as index-controller]))

; This is a placeholder cache until I have a better system (memcached?)
; Need to remember to purge this when a new post is added to the blog
(def index-cache (atom nil))

(html/defsnippet 
  post-template 
  (File. (str (pwd) "/templates/post-snippet.html"))
  [:div]
  [post-entry]
  [:h3] (html/html-content (:title post-entry))
  [:h4] (html/html-content (:date post-entry))
  [:p] (html/html-content (:body post-entry)))

(html/deftemplate index-template  (File. (str (pwd) "/templates/index.html"))
  [all-posts]
  [:div#posts] (html/html-content (apply str (map #(apply str (html/emit* (post-template %))) all-posts))))

(def *posts* [
  {:title "Post 1" :date "today" :body "this is the body"}
  {:title "Post 2" :date "today" :body "this is the body"}
  {:title "Post 3" :date "today" :body "this is the body"}
  {:title "Post 4" :date "today" :body "this is the body"}])

(defn render-view [channel request]
  (if (= nil @index-cache)
    (reset! index-cache (apply str (index-template *posts*))))
  (enqueue-and-close channel 
    {:status 200 
     :headers {"Content-Type" "text/html"}
     :body @index-cache}))
