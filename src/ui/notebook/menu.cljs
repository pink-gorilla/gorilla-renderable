(ns ui.notebook.menu
  (:require
   [re-frame.core :as rf]
   [ui.notebook.tooltip :refer [with-tooltip]]))

(defn header-ico [fa-icon rf-dispatch]
  [:a {:on-click #(rf/dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa-lg pl-1 " fa-icon)}]])

(defn header-icon [fa-icon rf-dispatch text]
  [with-tooltip text [header-ico fa-icon rf-dispatch]])

(defn menu []
  [:<>
   [header-icon "far fa-file" [:document/new] "new notebook"]
   [header-icon "fab fa-windows" [:notebook/layout-toggle] "layout toggle"]
   [header-icon "far fa-calendar" [:notebook/clear-all] "clear all output"]
   [header-icon "fa fa-microchip" [:notebook/evaluate-all] "evaluate all code"]
   [header-icon "fa fa-plus" [:segment/add] "add segment"]

   [header-icon "fas fa-arrow-up" [:notebook/move :up] "move up"]
   [header-icon "fas fa-arrow-down" [:notebook/move :down] "move down"]])

