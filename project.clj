(defproject quick_research "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [enlive "1.1.1"]
                 [spyscope "0.1.5"]
                 [clj-http "2.0.1"]
                 [org.clojure/data.csv "0.1.3"]
                 ]
  :main ^:skip-aot quick-research.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
