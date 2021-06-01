(ns picasso.data.notebook)

(def notebook
  {:meta {:id :7c9ab23f-c32f-4879-b74c-de7835ca8ba4
          :title "demo 123"
          :tags #{:demo :simple}}
   :segments
   [{:id 1
     :type :md
     :data "# hello, world\n- iii\n- ooo"
     :state nil}
    {:id 2
     :type :code
     :data {:kernel :clj
            :code "(+ 7 7)"}
     :state nil}
    {:id 3
     :type :md
     :data "# edn kernel evals\nyes -edn for testing!"
     :state nil}
    {:id 4
     :type :code
     :data {:kernel :edn
            :code "13"}
     :state {:id 15
             :picasso {:type :hiccup
                       :content [:span {:class "clj-long"} 13]}}}
    {:id 5
     :type :code
     :data {:kernel :edn
            :code "{:a 1 :b [7 8]}"}}

    {:id 6
     :type :code
     :data {:kernel :clj
            :code "(clock-svg 12 15)"}
     :state {:ns "user"
             :out ""
             :err []
             :root-ex nil
             ;:datafy "{:idx 2, :value [:sv … ]], :meta {:R true}}"
             ;:value ["[:svg {:height 100, … , :stroke-width 3}]]"]
             :picasso {:type :reagent
                       :content {:hiccup [:svg
                                          {:height 100, :width 100}
                                          [:circle {:cx 50, :cy 50, :r 40, :stroke "black", :stroke-width 4}]
                                          [:line {:x1 50, :y1 50
                                                  :x2 51.15193713515706, :y2 20.022124144018374
                                                  :stroke "red", :stroke-width 4}]
                                          [:line {:x1 50, :y1 50
                                                  :x2 84.99999999976387, :y2 49.99987143782138
                                                  :stroke "black", :stroke-width 4}]]}}}}

    {:id 7
     :type :md,
     :data "# Welcome to PinkGorilla Notebook↵
↵
## Features↵
↵
* clojure evals (via nrepl)↵
↵
* clojurescript evals (via goldly and sci)↵
↵
* ui visualizations↵
↵
* dynamically add new custom ui vizualisations↵
↵
* save notebooks to file /github gist or github rep, :id :1a1f36af-dea8-4e5b-8a0a-c91d33a224d9}"}

    {:id 8
     :type :code
     :data {:kernel :edn
            :code "[7 8]"}
     :state {:id 16
             :picasso {:type :list-like
                       :content {:class "clj-vector"
                                 :open "["
                                 :close "]"
                                 :separator  ", "
                                 :items [{:type :hiccup
                                          :content [:span {:class "clj-long"} 7]}
                                         {:type :hiccup
                                          :content [:span {:class "clj-long"} 8]}]
                                 :value [7 8]}}}}]})
