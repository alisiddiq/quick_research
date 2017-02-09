(defproject quick_research "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [enlive "1.1.1"]
                 [ring "1.2.0"]
                 [net.cgrand/moustache "1.1.0"]
                 [com.novemberain/monger "3.0.0-rc2"]
                 [spyscope "0.1.5"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [com.taoensso/timbre "4.2.0"]
                 [clj-webdriver "0.7.2"]
                 [clj-http "2.0.1"]
                 [io.forward/clojure-mail "1.0.4"]
                 [com.draines/postal "1.11.3"]
                 [org.jsoup/jsoup "1.8.1"]
                 [org.json/json "20151123"]
                 [org.apache.httpcomponents/httpclient "4.5"]
                 [com.github.detro.ghostdriver/phantomjsdriver "1.0.3"]
                 [twitter-api "0.7.8"]
                 [org.clojure/data.csv "0.1.3"]
                 ]
  :main ^:skip-aot quick-research.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
