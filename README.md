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
[{:date "2016-06-29", :price-change 5.0315182089795485, :news []}
 {:date "2016-09-29", :price-change 4.3377674956622325, :news []}
 {:date "2016-11-01",
  :price-change -4.47591967394866,
  :news [("Nils Andersen to join the BP Board"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/nils-andersen-to-join-the-bp-board/201611010705039265N/")
         ("Press release: BP third quarter 2016 results"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/press-release--bp-third-quarter-2016-results/201611010700089361N/")
         ("3Q16 Part 1 of 1" "http://www.investegate.co.uk/bp-plc--bp--/rns/3q16-part-1-of-1/201611010700079263N/")]}
 {:date "2017-02-07",
  :price-change -4.081414854636399,
  :news [("Press release: BP full year and 4Q 2016 results"
          "http://www.investegate.co.uk/bp-plc--bp--/rns/press-release--bp-full-year-and-4q-2016-results/201702070700081777W/")
         ("4Q16 Part 1 of 1" "http://www.investegate.co.uk/bp-plc--bp--/rns/4q16-part-1-of-1/201702070700071699W/")]}]

```

## Usage from JAR file
```Java
java -jar quick_research.jar <COMPANY CODE> <SINCE-DATE yyyy-MM-dd> <PRICE CHANGE THRESHOLD>
```
