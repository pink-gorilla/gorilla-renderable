(ns ui.notebook.view.menu
  (:require
   [re-frame.core :as rf]
   [picasso.data.document :as data]))

(defn link-fn [fun text]
  [:a.bg-blue-300.cursor-pointer.hover:bg-red-700.m-1
   {:on-click fun} text])

(defn link-dispatch [rf-evt text]
  (link-fn #(rf/dispatch rf-evt) text))

(defn link-href [href text]
  [:a.bg-blue-300.cursor-pointer.hover:bg-red-700.m-1
   {:href href} text])

(defn menu []
  [:div
   [:span [link-href "/" "main"]]
   [:span [link-dispatch [:doc/add data/document] "load"]]

   [:span [link-dispatch [:notebook/move :to 1] "activate 1"]]
   [:span [link-dispatch [:notebook/move :to 8] "activate 8"]]

   [:span [link-dispatch [:doc/exec [:clear-all]] "clear all"]]
   [:span [link-dispatch [:doc/exec [:eval-all]] "eval all"]]
   [:span [link-dispatch [:notebook/layout-toggle] "layout"]]])