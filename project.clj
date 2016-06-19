(defproject binaryage/devtools "0.7.1-SNAPSHOT"
  :description "A collection of Chrome DevTools enhancements for ClojureScript developers."
  :url "https://github.com/binaryage/cljs-devtools"
  :license {:name         "MIT License"
            :url          "http://opensource.org/licenses/MIT"
            :distribution :repo}

  :scm {:name "git"
        :url  "https://github.com/binaryage/cljs-devtools"}

  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.76" :scope "provided"]]

  :clean-targets ^{:protect false} ["target"
                                    "test/resources/_compiled"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-shell "0.5.0"]]

  :source-paths ["src/lib"
                 "src/debug"]

  :test-paths ["test"]

  :cljsbuild {:builds {}}                                                                                                     ; prevent https://github.com/emezeske/lein-cljsbuild/issues/413

  :profiles {:devel
             {:cljsbuild {:builds {:devel
                                   {:source-paths ["src/lib"
                                                   "src/debug"]
                                    :compiler     {:output-to     "target/devel/cljs_devtools.js"
                                                   :output-dir    "target/devel"
                                                   :optimizations :none
                                                   :source-map    true}}}}}

             :testing
             {:cljsbuild {:builds {:tests
                                   {:source-paths ["src/lib"
                                                   "test/src/tests"]
                                    :compiler     {:output-to     "test/resources/_compiled/tests/build.js"
                                                   :output-dir    "test/resources/_compiled/tests"
                                                   :asset-path    "_compiled/tests"
                                                   :optimizations :none
                                                   :pretty-print  true
                                                   :source-map    true}}
                                   :dead-code-elimination
                                   {:source-paths ["src/lib"
                                                   "test/src/dead-code-elimination"]
                                    :compiler     {:output-to       "test/resources/_compiled/dead-code-elimination/build.js"
                                                   :output-dir      "test/resources/_compiled/dead-code-elimination"
                                                   :asset-path      "_compiled/dead-code-elimination"
                                                   :closure-defines {"goog.DEBUG" false}
                                                   :pseudo-names    true
                                                   :optimizations   :advanced}}}}}
             :auto-testing
             {:cljsbuild {:builds {:tests
                                   {:notify-command ["lein" "run-phantom"]}}}}}

  :aliases {"test"           ["do"
                              "test-phantom,"
                              "test-dead-code"]
            "test-dead-code" ["do"
                              "with-profile" "+testing" "cljsbuild" "once" "dead-code-elimination,"
                              "shell" "test/scripts/dead-code-check.sh"]
            "test-phantom"   ["do"
                              "with-profile" "+testing" "cljsbuild" "once" "tests,"
                              "run-phantom"]
            "run-phantom"    ["shell" "phantomjs" "test/resources/phantom.js" "test/resources/runner.html"]
            "auto-test"      ["do"
                              "clean,"
                              "with-profile" "+testing,+auto-testing" "cljsbuild" "auto" "tests"]
            "release"        ["do"
                              "shell" "scripts/check-versions.sh,"
                              "clean,"
                              "test,"
                              "jar,"
                              "shell" "scripts/check-release.sh,"
                              "deploy" "clojars"]})
