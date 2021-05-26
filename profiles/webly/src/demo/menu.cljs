(ns demo.menu
  (:require
   [re-frame.core :as rf]
   [picasso.data.document :as data]
   [ui.notebook.menu]))

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
   [link-href "/" "main"]
   [link-dispatch [:doc/load data/document] "load"]

   [link-dispatch [:notebook/move :to 1] "activate 1"]
   [link-dispatch [:notebook/move :to 8] "activate 8"]

   [link-dispatch [:segment/new-above] "new above"]
   [link-dispatch [:segment/new-below] "new below"]

   [ui.notebook.menu/menu]

   ;[:span [link-dispatch [:doc/exec [:clear-all]] "clear all"]]
   ;[:span [link-dispatch [:doc/exec [:eval-all]] "eval all"]]
   ;[:span [link-dispatch [:notebook/layout-toggle] "layout"]]
   ])

(defn template-header-document [header document]
  [:div {:style {:display "grid"
                 :height "100vh"
                 :width "100vw"
                 :grid-template-columns "auto"
                 :grid-template-rows "30px auto"}}
   header
   ;[:div.overflow-auto.m-0.p-0
   ; {:style {:background-color "red"
   ;          :height "100%"
    ;         :max-height "100%"}}
   document]
;  ]
  )