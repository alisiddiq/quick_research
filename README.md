# quick_research

This repo allows the user to quickly get past daily price movements of a particular stock, along with a summary of company news (if any) for days that had large price movement.

It only shows RNS for UK listed stocks

## Usage from REPL
1. Load the repl
2. Then load the namespace in the repl

 ```Clojure
 (use 'quick-research.price_news)
 (in-ns 'quick-research.price_news)
 ```
 
3. Then call the function, specifying the company code, number of days for which prices should be checked, and the threshold level of price change for which results will be returned
 ```Clojure
 (quick-research "BP" "2016-06-01" 4) 
 ;Will return any dates at which price of BP moved by more than +/- 4 percent, since 01/06/2016
 ```
returns
```
[{:date "2016-06-28", :price-change -4.790484127791447, :news []}
 {:date "2016-09-28",
  :price-change -4.157427937915743,
  :news [("BP p.l.c. publishes provisional dividend dates"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/bp-p-l-c--publishes-provisional-dividend-dates/201609281426161052L/")]}
 {:date "2016-10-31",
  :price-change 4.68564539817714,
  :news [("Total Voting Rights"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/total-voting-rights/201610311602469036N/")]}
 {:date "2017-02-06", :price-change 4.255082420628972, :news []}
 {:date "2017-04-13",
  :price-change 4.08749442217197,
  :news [("BP plc Notice of Annual General Meeting 2017"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/bp-plc-notice-of-annual-general-meeting-2017/201704130927044455C/")]}]

```

## Usage from JAR file
```Java
java -jar quick_research.jar <COMPANY CODE> <SINCE-DATE yyyy-MM-dd> <PRICE CHANGE THRESHOLD>
```
