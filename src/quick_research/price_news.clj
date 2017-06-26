(ns quick-research.price_news
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.cookies :as cookies]
            [clojure.string :as str]
            [quick-research.utils :as utils]
            [spyscope.core :refer :all]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-time.format :as f]
            [clojure.data.csv :as csv]))


(defn- get-cookies
  [code]
  (let [url (str "https://finance.yahoo.com/quote/" code "/history?p=" code)
        cookie-jar (cookies/cookie-store)
        http-request (:body (utils/http-request :get url :cookie-store cookie-jar))
        crumb (last (str/split (re-find #"\"CrumbStore\":\{\"crumb\":\".{11}" http-request) #"\""))
        cookie-val (:value (get (clj-http.cookies/get-cookies cookie-jar) "B"))
        ]
    {:cookie cookie-val :crumb crumb}
    )
  )

(defn get-yahoo-prices
  "Get prices from Yahoo, start and end dates should be in the format yyyy-MM-dd, if quotes are for lse, supply appropriate flag"
  [code start end & {:keys [lse? interval retry-attempts sleep-time]}]
  (let [code (str/replace code ".L" "")
        lse? (or lse? true)
        interval (or interval "1d")
        retry-attempts (or retry-attempts 10)
        sleep-time (or sleep-time 10)
        code (if lse? (str code ".L") code)
        cookies (get-cookies code)
        start-date (quot (tc/to-long (f/parse (f/formatters :date) start)) 1000)
        end-date (quot (tc/to-long (f/parse (f/formatters :date) end)) 1000)
        url (str "https://query1.finance.yahoo.com/v7/finance/download/" code "?period1=" start-date "&period2=" end-date "&interval=" interval "&events=history&crumb=" (:crumb cookies))
        ]
    (if-not (zero? retry-attempts)
      (try (csv/read-csv (:body (utils/http-request
                                  :get url
                                  :headers {"Cookie" (str "B=" (:cookie cookies))}
                                  )))
           (catch Exception e
             (do (println (str "Exception found: " (.getMessage e) ", retrying after " sleep-time " seconds..."))
                 (Thread/sleep (* sleep-time 1000))
                 (get-yahoo-prices code start end :lse? lse? :interval interval :retry-attempts (dec retry-attempts) :sleep-time sleep-time)
                 )))
      (println "Failed after re-attempts :( exiting...")
      )
    ))

(defn get-perc-changes
  [code since]
  (let [now-str (f/unparse (f/formatter "yyyy-MM-dd") (t/now))
        past-price-data (utils/tc #(rest (get-yahoo-prices code since now-str :lse? true)))
        csv-data (if-not (nil? past-price-data)
                   (mapv #(read-string (nth % 4)) (vec past-price-data))
                   nil)
        csv-data-old (vec (rest csv-data))
        csv-data-new (vec (butlast csv-data))
        perc-change (mapv utils/perc-change csv-data-new csv-data-old)
        map-fn (fn [price-entry change] [(keyword (first price-entry)) change])
        perc-change-date (into (sorted-map) (mapv map-fn past-price-data perc-change))
        ]
    perc-change-date
    ))

(defn get-news
  [code date]
  (let [date-str (str/replace date #"-" "")
        code (str/lower-case code)
        hostname "http://www.investegate.co.uk"
        full-news-list (utils/fetch-url (str hostname "/Index.aspx?&arch=1&limit=-1&date=" date-str))
        news-links (map #(-> % :attrs :href) (html/select full-news-list [:a.annmt]))
        full-links (map #(str hostname %) news-links)
        summaries (html/select full-news-list [:a.annmt html/text-node])
        combined (map list summaries full-links)
        valid (vec (filter #(.contains (nth % 1) (str "--" code "-")) combined))
        ]
    valid
    )
  )

(defn add-news
  [code date-price]
  (let [date (name (first date-price))
        price (second date-price)
        news (get-news code date)
        ]
    {:date date
     :price-change price
     :news news}
    )
  )

(defn quick-research
  [code since threshold]
  (let [past-prices (get-perc-changes code since)
        filtered-prices (filter #(> (Math/abs (val %)) threshold) past-prices)
        with-news (vec (pmap #(add-news code %) filtered-prices))
        ]
    with-news
    )
  )

