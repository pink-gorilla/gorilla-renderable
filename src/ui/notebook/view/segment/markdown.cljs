(ns ui.notebook.view.segment.markdown
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :as rf]
   [webly.build.lazy :refer-macros [wrap-lazy]]
   ;[ui.markdown.viewer :refer [markdown-viewer]] 
   ;[ui.markdown.prosemirror :refer [prosemirror-reagent]]
   [ui.notebook.view.segment-menu :refer [cell-menu]]))

(def markdown-viewer (wrap-lazy ui.markdown.viewer/markdown-viewer))
(def prosemirror-reagent (wrap-lazy ui.markdown.prosemirror/prosemirror-reagent2))

(def pm-fun {:get-data (fn [id]
                         (let [s (rf/subscribe [:notebook/segment id])]
                           (if s
                             (do (info "md sub id: " id  "is: " @s)
                                 (or (get-in @s [:data]) "xxx sub was empty"))
                             "empty md")))
             :save-data (fn [id text]
                          (info "pm-text save")
                          (rf/dispatch [:doc/exec [:set-md-segment id text]]))})

(defn md-segment-view
  [{:keys [id data]} active?]
  [markdown-viewer {:on-click #(rf/dispatch [:notebook/move :to id])} data])

(defn md-segment-edit
  [{:keys [id data]} active?]
  [prosemirror-reagent id pm-fun active?])

(defn md-segment
  [nb-settings {:keys [id] :as seg}]
  (let [segment-active (rf/subscribe [:notebook/segment-active])]
    (fn [nb-settings {:keys [id] :as seg}]
      (let [active? (= (:id @segment-active) id)]
        [:div ;{;:class  (str "segment md"
               ;             (if active? " selected" ""))
               ;:on-click #(rf/dispatch [:notebook/move :to id])}
         (if active?
           [:<> [md-segment-edit seg active?]
            [cell-menu seg]]
           [md-segment-view seg active?])]))))

