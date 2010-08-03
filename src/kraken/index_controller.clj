(ns kraken.index-controller
  (:import [java.io File])
  (:use [aleph.core]
        [clojure.contrib.duck-streams :only [pwd]])
  (:require [net.cgrand.enlive-html :as html]
            [kraken.index-controller :as index-controller]))

(html/deftemplate index (File. (str (pwd) "/templates/index.html"))
  []
  [:div#posts] (html/content "This is a post."))

(defn render-view [channel request]
  (enqueue-and-close channel {:status 200 :body (apply str (index))}))
