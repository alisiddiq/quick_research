(ns quick-research.price_news
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [quick-research.utils :as utils]
            [spyscope.core :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.data.csv :as csv]))


(defn get-past-price-data-yahoo
  [code days]
  (let [end-date-str (f/unparse (f/formatter "dd-MM-yyyy") (t/now))
        start-date-str (f/unparse (f/formatter "dd-MM-yyyy") (t/plus (t/now) (t/days (* -1 days))))

        splitted-end-date (str/split end-date-str #"-")
        splitted-start-date (str/split start-date-str #"-")
        code (str (str/replace code "." "") ".L")
        start-day-str (first splitted-start-date)
        start-month (dec (read-string (str/replace (second splitted-start-date) #"^0" "")))
        start-month-str (if (< start-month 10)
                          (str "0" start-month)
                          (str start-month))
        start-year-str (last splitted-start-date)
        end-day-str (first splitted-end-date)
        end-month (dec (read-string (str/replace (second splitted-end-date) #"^0" "")))
        end-month-str (if (< end-month 10)
                        (str "0" end-month)
                        (str end-month))
        end-year-str (last splitted-end-date)

        csv-data (:body (utils/http-request :get "http://real-chart.finance.yahoo.com/table.csv" :query-params
                                      {"s" code
                                       "a" start-month-str
                                        "b" start-day-str
                                        "c" start-year-str
                                        "d" end-month-str
                                        "e" end-day-str
                                        "f" end-year-str
                                        "g" "d"
                                        "ignore" ".csv"}))
        parsed (csv/read-csv csv-data)
        ]
    parsed
    ))

(defn get-perc-changes
  [code days]
  (let [past-price-data (utils/tc #(rest (get-past-price-data-yahoo code (+ 1 days))))
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
  [code days threshold]
  (let [past-prices (get-perc-changes code days)
        filtered-prices (filter #(> (Math/abs (val %)) threshold) past-prices)
        with-news (vec (pmap #(add-news code %) filtered-prices))
        ]
    with-news
    )
  )

