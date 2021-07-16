(ns ui.notebook.loader.page
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [picasso.default-config] ; side-effects
   [ui.notebook.core :refer [notebook-view]]
   [ui.notebook.menu]
   [ui.notebook.settings] ; side effects
   [ui.notebook.loader.sidebar :refer [sidebar]]
   [ui.notebook.loader.list] ; side-effects
   ))
(defn page-notebook [menu]
  [:div.grid
   {:style {:height "100vh"
            :min-height "100vh"
            :max-height "100vh"
            :width "100vw"
            :min-width "100vw"
            :max-width "100vw"
            :grid-template-rows "2.5em 100%"
            :overflow "hidden"}}
   [:div.w-full.bg-gray-50.pt-3
    [menu]]
   [:div.grid.h-full.min-h-full.max-h-full
    {:style {:grid-template-columns "8em 100%"}}
    [:div.min-h-full.max-h-full
     [sidebar]]
    [notebook-view]]])