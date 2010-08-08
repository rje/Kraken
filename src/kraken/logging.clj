(ns kraken.logging
  (:import [java.io PrintWriter]
           [java.util Date]
           [java.text SimpleDateFormat]))

(def *log-file-path* (ref nil))
(def *log-writer* (ref nil))
(def *log-level* (ref 2))

(defn set-logging-level
  "Available keywords: :none :error :warning :info :debug, each higher 
   level includes the previous levels.  Defaults to :warning."
  [level-keyword]
  (dosync
    (cond 
      (= :error level-keyword) (ref-set *log-level* 1)
      (= :warning level-keyword) (ref-set *log-level* 2)
      (= :info level-keyword) (ref-set *log-level* 3)
      (= :debug level-keyword) (ref-set *log-level* 4)
      :else (ref-set *log-level* 0))))


(defn set-logging-file [path-to-file]
  (dosync
    (ref-set *log-file-path* path-to-file)
    (ref-set *log-writer* (PrintWriter. @*log-file-path*))))

(def *date-formatter* (SimpleDateFormat. "yyyy-MM-dd HH:mm:ss"))

(defn- get-date-string []
  (-> *date-formatter* (.format (Date.))))

(defn- level->value [level]
  (let [value-map {:none 0, :error 1, :warning 2, :info 3, :debug 4}]
    (level value-map)))

(defn log [level message]
  (if (and (not= @*log-writer* nil) (<= (level->value level) @*log-level*))
      (do
        (-> @*log-writer* (.println (str (get-date-string) ": " message)))
        (-> @*log-writer* .flush))))
