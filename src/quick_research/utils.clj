(ns quick-research.utils
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))


(defn abs [n] (max n (- n)))

(defn perc-change [new-value old-value & [abs-flag]]
  (if (= abs-flag 1)
    (double (* (/ (abs (- new-value old-value)) old-value) 100))
    (double (* (/ (- new-value old-value) old-value) 100))
    ))

(defn tc [function]
  (try
    (function)
    (catch Exception e
      (do (println "WARN: " (.getMessage e))
          nil))))

(defn fetch-url [url]
  (with-open [inputstream (-> (java.net.URL. url)
                              .openConnection
                              (doto (.setRequestProperty "User-Agent"
                                                         "Mozilla/5.0 ..."))
                              .getContent)]
    (html/html-resource inputstream)))

(defn http-request
  [method url & {:keys [form-params query-params content-type cookies headers]}]
  (client/request {:url          url
                   :method       method
                   :form-params  form-params
                   :query-params query-params
                   :content-type content-type
                   :as           content-type
                   :cookie-store cookies
                   :headers      headers}
                  )
  )