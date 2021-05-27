(ns demo.pages.doc
  (:require
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [picasso.data.document :as data] ; sample-data
   [ui.notebook.core :refer [notebook-view]]
   [demo.menu :refer [template-header-document menu]]
   ))

(rf/dispatch [:doc/load data/document])

(def opts
  {
   ; if a layout option is passed this will override the settings in localstorage
   ;:layout :single ; :vertical ; :horizontal
   :view-only true
   })

(defmethod reagent-page :notebook/current [{:keys [route-params query-params handler] :as route}]
  [template-header-document
    [menu]
    [notebook-view opts]])


(rf/reg-event-fx
 :document/new
 (fn [_ [_]]
     (rf/dispatch [:doc/new])))
