(ns blog.posts 
  (:require [com.ashafa.clutch :as couchdb]))

(def *blog-database*
  (couchdb/get-database {:name "blog"
                 :language "clojure"}))

(defn get-post-with-title [title]
  (couchdb/with-db *blog-database*
    (:value (first (:rows 
      (couchdb/get-view "posts" :posts-with-title {:key title}))))))

(defn get-posts-by-date []
  (couchdb/with-db *blog-database*
    (:rows 
       (couchdb/get-view "posts" :all-posts {:descending true, :limit 10}))))
