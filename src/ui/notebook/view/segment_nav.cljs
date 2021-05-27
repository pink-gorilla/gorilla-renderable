(ns ui.notebook.view.segment-nav
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [subscribe dispatch]]
   [picasso.document.index :refer [segment-ids-ordered]]))

(defn icon [active-segment-id current-segment-id]
  (let [active? (= active-segment-id current-segment-id)]
    (if active?
      [:i.fas.fa-circle.ml-1]
      [:i.far.fa-circle.ml-1
       {:on-click #(dispatch [:notebook/move :to current-segment-id])}])))

(defn segment-nav []
  (let [notebook (subscribe [:notebook])
        active-segment (subscribe [:notebook/segment-active])]
    (fn []
      (let [active-segment @active-segment
            active-segment-id (:id active-segment)
            order (segment-ids-ordered @notebook)]
        (warn "segment nav current: " active-segment-id " order: " order)
        (into [:div.bg-blue-200]
              (map (partial icon active-segment-id) order))))))
