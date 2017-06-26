(ns quick-research.core
  (:require [quick-research.price_news :as p])
  (:gen-class :main true))

(defn -main
  [& args]
  (let [args-length (count args)]
    (case args-length
      0 (println "Usage  <COMPANY CODE> <SINCE DATE yyyy-MM-dd> <PRICE CHANGE THRESHOLD>")
      1 (println "Usage  <COMPANY CODE> <SINCE DATE yyyy-MM-dd> <PRICE CHANGE THRESHOLD>")
      2 (println "Usage  <COMPANY CODE> <SINCE DATE yyyy-MM-dd> <PRICE CHANGE THRESHOLD>")
      3 (clojure.pprint/pprint (p/quick-research (nth args 0) (str (nth args 1)) (read-string (str (nth args 2)))))
      )))
