(defproject kraken "0.2.0-SNAPSHOT"
  :description "A simple clojure based web framework"
  :dependencies [[org.clojure/clojure "1.2.0-RC2"]
                 [org.clojure/clojure-contrib "1.2.0-RC2"]
                 [org.clojars.rje/aleph "0.1.0-SNAPSHOT"]
                 [enlive "1.0.0-SNAPSHOT"]
                 [org.clojars.rje/clutch "0.2.1"]]
  :jvm-opts ["-server" "-Xmx300M" "-XX:+UseParNewGC"])
