(ns ui.notebook.loader.sidebar
  (:require
   [taoensso.timbre :as timbre :refer-macros [debugf info error]]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [ui.notebook.loader.load :refer [load-notebook]]
   ui.notebook.loader.list ; side-effects
   ))
#_(defn sidebar
    [visible? set-notebook notebook-name notebook-names]
    (let [width 200]
      [:div.fixed.z-5.top-0.bottom-0.flex.flex-column.bg-white.br
       {:style {:width      width
                :transition "all ease 0.2s"
                :left       (if visible? 0 (- width))}}

       [:div.flex.items-stretch.pl1.flex-none.bg-blue-100
        [:div;.flex-auto
         ]

        [:div ;.overflow-y-auto.bg-white.pb4
         [notebooks-list set-notebook notebook-name notebook-names]]]]))

(defn nb-doc [{:keys [name active? open-nb] :as nb}]
  [:a.hover:bg-red-700.hover:bg-opacity-50
   {:class (str "m-1 p-1 border-round border-1 border-purple-500 shadow "
                "hover:border-gray-500 "
                (if (active? name)
                  "bg-blue-200"
                  "bg-yellow-100"))
    :on-click  #(open-nb nb)}
   name])

(defn nb-list [active? open-nb {:keys [name notebooks]}]
  [:<>
   [:p.text-xl.text-blue-500.ml-2 name]
   (for [nb notebooks]
     ^{:key (:name nb)}
     [nb-doc (assoc nb
                    :active? active?
                    :open-nb open-nb)])])
(defn sidebar []
  (let [nb-lists (rf/subscribe [:notebook-lists])
        name-active (r/atom nil)
        open-nb (fn [nb]
                  (reset! name-active (:name nb))
                  (load-notebook nb))
        active? (fn [name]
                  (= name @name-active))]
    (fn []
      (let [nb-lists (vals @nb-lists)]
        (into
         [:div.flex.flex-col.items-stretch.h-full.bg-gray-50.w-full]
         (map (partial nb-list active? open-nb) nb-lists))))))