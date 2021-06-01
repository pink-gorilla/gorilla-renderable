(ns notebook.commands
  (:require
   [taoensso.timbre :as timbre :refer [debug debugf info error]]
   [re-frame.core :as rf]
   [notebook.position :as pos]))

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
 :notebook/meta-set
 (fn [_ [_ k v]]
   (info "changing notebook meta " k " to: " v)
   (rf/dispatch [:doc/exec [:set-meta-key k v]])))

; segment

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
   (rf/dispatch [:doc/exec [:eval-segment-active]])))

(rf/reg-event-fx
 :segment/add
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:add-code :clj ""]])))

(rf/reg-event-fx
 :segment/new-above
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:insert-before]])))

(rf/reg-event-fx
 :segment/new-below
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:insert-below]])))

(rf/reg-event-fx
 :segment-active/delete
 (fn [_ _]
   (info "delete active segmentl")
   (rf/dispatch [:doc/exec [:remove-segment-active]])))

; position


(rf/reg-event-fx
 :notebook/move
 (fn [_ [_ direction id]]
   (rf/dispatch [:doc/exec [:move direction id]])))

(rf/reg-event-fx
 :segment/clear
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:clear-segment-active]])))

(rf/reg-event-fx
 :segment/type-toggle
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:toggle-type-segment-active]])))

(rf/reg-event-fx
 :segment/move-pos-down
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:move-active-segment-down]])))

(rf/reg-event-fx
 :segment/move-pos-up
 (fn [_ [_]]
   (rf/dispatch [:doc/exec [:move-active-segment-up]])))

(rf/reg-sub
 :notebook/segment-active
 :<- [:notebook/current]
 (fn [notebook _]
   (pos/segment-active notebook)))