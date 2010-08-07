(ns blog.config 
  (:use [kraken.config])
  (:require [blog.index-controller :as index-controller]
           [blog.post-controller :as post-controller]))

(set-port 8080)

(set-routes { 
  #"^/$" index-controller/render-view,
  #"^/post/" post-controller/render-view })
