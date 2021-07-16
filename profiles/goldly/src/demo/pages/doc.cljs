(ns demo.pages.doc
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [picasso.data.notebook :as data] ; sample-data
   [ui.notebook.loader.page :refer [page-notebook]]
    [demo.site :refer [template-header-document menu]]
   ))

(def nb-list-embed
  {:name "cljs"
   :notebooks [{:name "a"
                :type :embedded
                :data data/notebook}
               {:name "b"
                :type :embedded
                :data data/notebook}
               {:name "t"
                :type :template}
               ]})

(rf/dispatch [:doc/load data/notebook])

(rf/dispatch [:notebook-list/set nb-list-embed])


(defmethod reagent-page :notebook/current [{:keys [route-params query-params handler] :as route}]
  [page-notebook menu])


(rf/reg-event-fx
 :document/new
 (fn [_ [_]]
     (rf/dispatch [:doc/new])))
