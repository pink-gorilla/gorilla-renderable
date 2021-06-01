(ns ui.notebook.view.segment-menu
  (:require
   [re-frame.core :as rf]))

(defn link-fn [fun text]
  [:a.bg-blue-300.cursor-pointer.hover:bg-red-700.m-1
   {:on-click fun} text])

(defn link-dispatch [rf-evt text]
  (link-fn #(rf/dispatch rf-evt) text))

(defn fa-dispatch [fa d]
  [:a {:class    [:lg:inline-block :lg:mt-0]
       :on-click #(rf/dispatch d)}
   [:i {:class fa}]])

(defn cell-menu [segment]
  [:div {:class [:font-sans :flex :flex-col :text-center :sm:flex-row :sm:text-left :sm:justify-between :px-6 :bg-white :sm:items-baseline :w-full]}

   [:div.mb-1
    [link-dispatch [:segment/new-above] "insert above"]
    [link-dispatch [:segment/new-below] "insert below"]
    [link-dispatch [:segment-active/delete] "delete"]
    [link-dispatch [:segment/type-toggle] "toggle editor"]

    [fa-dispatch [:fas :fa-arrow-circle-down] [:segment/move-pos-down]]
    [fa-dispatch [:fas :fa-arrow-circle-up] [:segment/move-pos-up]]]

   (when (= (:type segment) :code)
     [:div.mt-1.mb-1.h-8
   ;[:p {:class "text-lg no-underline text-grey-darkest hover:text-blue-dark ml-2"} "One" ]
      [link-dispatch [:segment-active/eval] "Evaluate"]
      [link-dispatch [:segment/clear] "Clear Output"]])

   ;; Move Segment around.
   [:div.text-lg
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(rf/dispatch [:notebook/move :up])}
     [:i.fas.fa-arrow-circle-up]]
    [:a {:class    [:pg-cell-move :lg:inline-block :lg:mt-0]
         :on-click #(rf/dispatch [:notebook/move :down])}
     [:i.fas.fa-arrow-circle-down]]]])