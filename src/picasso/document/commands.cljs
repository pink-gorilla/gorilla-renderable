(ns picasso.document.commands
  (:require
   [taoensso.timbre :as timbre :refer [debug debugf info error]]
   [re-frame.core :as rf]))

; shorter keybindings for notebook


(rf/reg-event-fx
 :notebook/evaluate-all
 (fn [_ _]
   (rf/dispatch [:doc/exec [:eval-all]])))

(rf/reg-event-fx
 :notebook/clear-all
 (fn [_ _]
   (rf/dispatch [:doc/exec [:clear-all]])))

(rf/reg-event-fx
 :segment/kernel-toggle
 (fn [_ [_ id]]
   (rf/dispatch [:doc/exec [:kernel-toggle id]])))

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

(rf/reg-event-fx
 :segment/add
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:add-code :clj ""]])))

;:segment/new-below

#_(defn insert-segment
    [index-fn notebook]
    (let [{:keys [active order]} notebook
          active-idx (.indexOf order active)
          new-segment (create-code-segment "")]
      (insert-segment-at notebook (index-fn active-idx) new-segment)))

(rf/reg-event-fx
 :segment/new-above
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:insert-before]])))

(rf/reg-event-fx
 :segment/new-below
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:insert-below]])))


