(ns ui.notebook.view.segment.code
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :as rf]
   [ui.notebook.view.segment-menu :refer [cell-menu]]
   [webly.build.lazy :refer-macros [wrap-lazy]]
   ;[ui.code.goldly.codemirror-themed  :refer [codemirror-themed]]
   ))
(def codemirror-themed (wrap-lazy ui.code.goldly.codemirror-themed/codemirror-themed2))

(def cm-fun {:get-data (fn [id]
                         (let [s (rf/subscribe [:notebook/segment id])]
                           (if s
                             (do (debug "cm sub id: " id  "is: " @s)
                                 (or (get-in @s [:data :code]) "xxx sub was empty"))
                             "empty code")))
             :save-data (fn [id text]
                          (debug "cm-text save")
                          (rf/dispatch [:doc/exec [:set-code-segment id text]]))

             :cm-events (fn [[type & args]]
                          (debug "cm event type: " type " args: " args)
                          ;[:cm/mouse-down 3 {"left" 33, "right" 33, "top" 333, "bottom" 351}
                          (case type
                            :cm/mouse-down (rf/dispatch [:notebook/move :to (first args)])
                            :cm/move (rf/dispatch [:notebook/move (second args)])
                            (warn "unhandled cm event: " type)))})

(defn segment-code-edit [{:keys [active?] :as cm-opts} {:keys [id data] :as seg}]
  [:div {:style {:position "relative"}} ; container for kernel absolute position
   ; kernel - one color for both dark and light themes.
   (when-let [kernel (:kernel data)] ; snippets might not define kernel
     [:span.pr-2.text-right.text-blue-600.tracking-wide.font-bold.border.border-blue-300.rounded.cursor-pointer.hover:bg-red-700.m-1
      {:on-click #(rf/dispatch [:segment/kernel-toggle id])
       :style {:position "absolute"
               :z-index 200 ; dialog is 1040 (we have to be lower)
               :top "2px" ; not too big, so it works for single-row code segments
               :right "10px"
               :width "50px"
               :height "22px"}} kernel])
   (if active?
     [codemirror-themed id cm-fun cm-opts]
     [codemirror-themed id cm-fun cm-opts])])

(defn segment-code [{:keys [view-only] :as nb-settings} {:keys [id type] :as seg}]
  (let [settings (rf/subscribe [:settings])
        segment-active (rf/subscribe [:notebook/segment-active])
        segment-queued (rf/subscribe [:segment/queued? id])]
    (fn [{:keys [view-only] :as nb-settings} {:keys [id type] :as seg}]
      (let [active? (= (:id @segment-active) id)
            queued? @segment-queued
            full? (= (:layout @settings) :single)
            cm-opts  {:active? active?
                      :full? full?
                      :view-only view-only}]

        [:div.text-left.bg-gray-100 ; .border-solid
         {:id id
          ;:on-click #(do
          ;             (rf/dispatch [:notebook/move :to id]))
          :class (str (when queued?                         " border border-solid border-green-800")
                      (when (and active? (not view-only))       " border border-solid border-red-600")
                      (when (and active? view-only) " border border-solid border-gray-600")
                      (when full? " h-full"))}

         [segment-code-edit cm-opts seg]
         ;[:p "view-only: " (str view-only) "  active: " (str active?)]
         (when active? [cell-menu seg])]))))