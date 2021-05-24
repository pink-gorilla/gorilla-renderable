(ns demo.pages.doc
  (:require
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [picasso.data.document :as data] ; sample-data
   [ui.notebook.core :refer [notebook-view]]
   ))

(rf/dispatch [:doc/load data/document])

(def opts
  {
   ; if a layout option is passed this will override the settings in localstorage
   ;:layout :single ; :vertical ; :horizontal
   :view-only true
   })

(defmethod reagent-page :notebook/current [{:keys [route-params query-params handler] :as route}]
  [notebook-view opts])
