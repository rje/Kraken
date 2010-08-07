(ns blog.core
  (:gen-class)
  (:require [kraken.core]
            [blog.config]))


(defn -main [& args]
  (kraken.core/start-server))
