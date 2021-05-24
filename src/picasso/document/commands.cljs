(ns picasso.document.commands
  (:require
   [taoensso.timbre :as timbre :refer [debug debugf info error]]
   [re-frame.core :as rf]))

; shorter keybindings for notebook


(rf/reg-event-fx
 :segment-active/kernel-toggle
 (fn [_ _]
   (rf/dispatch [:doc/exec [:kernel-toggle-active]])))

(rf/reg-event-fx
 :segment-active/eval
 (fn [_ _]
   (rf/dispatch [:doc/exec [:kernel-toggle-active]])))

(rf/reg-event-fx
 :notebook/meta-set
 (fn [_ [_ k v]]
   (info "changing notebook meta " k " to: " v)
   (rf/dispatch [:doc/exec [:set-meta-key k v]])))

#_(defn insert-segment
    [index-fn notebook]
    (let [{:keys [active order]} notebook
          active-idx (.indexOf order active)
          new-segment (create-code-segment "")]
      (insert-segment-at notebook (index-fn active-idx) new-segment)))

#_(reg-event-db
   :segment/new-above
   (fn [db _]
     (notebook-op
      db
      (partial insert-segment identity))))

#_(reg-event-db
   :segment/new-below
   (fn [db _]
     (notebook-op
      db
      (partial insert-segment inc))))
