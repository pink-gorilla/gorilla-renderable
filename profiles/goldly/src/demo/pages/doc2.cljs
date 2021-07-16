(ns demo.pages.doc
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [picasso.data.notebook :as data] ; sample-data
   [ui.notebook.core :refer [notebook-view]]
   [demo.site :refer [template-header-document menu]]
   ))

(def nb-list-embed
  {:name "embedded"
   :notebooks [{:name "cljs"
                :type :embedded
                :data data/notebook}]})


(rf/dispatch [:doc/load data/notebook])

(def opts
  {
   ; if a layout option is passed this will override the settings in localstorage
   :layout :stacked ; :single ; :vertical ; :horizontal
   :view-only false
   })

(defn nb-page  []
  (let [f (r/atom true)]
    (when @f
      (rf/dispatch [:css/set-theme-component :codemirror "mdn-like"])
      ;(rf/dispatch [:css/set-theme-component :codemirror "base16-light"])
      (reset! f false) 
      )
  [template-header-document
   [menu]
   [notebook-view opts]]
  ))


(defmethod reagent-page :notebook/current [{:keys [route-params query-params handler] :as route}]
  [nb-page]
  )


(rf/reg-event-fx
 :document/new
 (fn [_ [_]]
     (rf/dispatch [:doc/new])))
