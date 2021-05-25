(ns ui.notebook.core
  (:require
   [re-frame.core :as rf]
   ; side-effects
   [picasso.kernel.view.picasso :refer [picasso-result]]
   [picasso.kernel.view.default-painter] ; side-effects
   [picasso.document.transactor] ;side-effects
   [picasso.document.position :refer [segment-active]] ;side-effects
   ; ui
   [ui.markdown.goldly.core] ;side-effects
   [ui.code.goldly.core] ;side-effects
   ; notebook
   [ui.notebook.view.layout :refer [notebook-layout]]
   [ui.notebook.settings] ; side-effects
   ))

(defn render-unknown [{:keys [id type data state] :as seg}]
  [:div.render-unknown
   [:p "segment id:" id "type " type]
   [:p "data: " (pr-str data)]
   [:p "state: " (pr-str state)]])

(defn seg-view [{:keys [md-view code-view] :as opts} {:keys [id type data state] :as seg}]
  (let [render (case type
                 :code code-view
                 :md md-view)]
    (if render
      [render id data state]
      [render-unknown seg])))

(defn doc-view
  [opts {:keys [id meta segments] :as document}]
  (let [seg-view (partial seg-view opts)]
    [:div
     [:p "meta: " (pr-str meta)]]
    (into [:div] (map seg-view segments))))

(defn notebook-view [opts]
  (let [doc (rf/subscribe [:notebook]) ; current notebook
        layout (rf/subscribe [:notebook/layout])]
    (fn [opts]
       ;[:div.w-full.h-full.min-h-full.bg-gray-100 ; .overflow-scroll
      [notebook-layout
       (merge {:layout @layout} opts)
       (segment-active @doc)
       (:segments @doc)])))

