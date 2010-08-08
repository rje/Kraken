(ns kraken.static-files
  (:import [java.io File])
  (:use [aleph.core]
        [kraken.logging]
        [clojure.contrib.duck-streams :only [pwd]]))

(defn get-public-file [filename]
  (File. (str (pwd) "/public" filename)))

(defn file-exists? [filename]
  (.exists (get-public-file filename)))

(defn return-404 [channel request]
  (let [file (get-public-file "/404.html")]
    (enqueue-and-close channel
      (if (.exists file)
        {:status 404, :body file}
        {:status 404, :body "This page was eaten by a grue"}))))

(defn return-file [channel request]
  (log :info (str "200: /public" (:uri request)))
  (let [file (get-public-file (:uri request))]
    (enqueue-and-close channel {:status 200, :body file})))
