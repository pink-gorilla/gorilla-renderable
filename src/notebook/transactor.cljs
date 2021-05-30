(ns notebook.transactor
  (:require
   [taoensso.timbre :as timbre :refer [debug debugf info error]]
   [re-frame.core :as rf]
   [notebook.eval :as eval]
   [notebook.transact :refer [fns-lookup transact]]
   [notebook.position :as pos]
   [notebook.core :as nb]
   [notebook.kernel :as k]
   [notebook.commands]))

(rf/reg-event-db
 :doc/add
 (fn
   [db [_ {:keys [meta segments] :as document}]]
   (let [doc-id (:id meta)
         db (if (:docs db)
              db
              (assoc db :docs {}))
         document (assoc document
                         :ns       nil  ; current namespace
                         :queued   #{} ; code segments that are qued for evaluation
                         :active   (:id (first segments)))]
     (debugf "Adding document: %s " doc-id)
     (assoc-in db [:docs doc-id] document))))

(rf/reg-event-db
 :doc/doc-active
 (fn [db [_ id]]
   (assoc db :doc-active id)))

(rf/reg-event-fx
 :doc/load
 (fn [_ [_ nb]]
   (rf/dispatch [:doc/add nb])
   (rf/dispatch [:doc/doc-active (get-in nb [:meta :id])])))

(rf/reg-event-fx
 :doc/new
 (fn [_ [_]]
   (let [nb (nb/new-notebook)
         nb (nb/add-code nb :clj "")]
     (rf/dispatch [:doc/add nb])
     (rf/dispatch [:doc/doc-active (get-in nb [:meta :id])]))))

(rf/reg-sub
 :doc/view
 (fn [db [_ id]]
   (get-in db [:docs id])))

#_(defn run [fun-args]
    (reset! doc (transact @doc fun-args))
    nil)

(rf/reg-event-db
 :doc/exec
 (fn [db [_ fun-args]]
   (if-let [id (:doc-active db)]
     (if-let [doc (get-in db [:docs id])]
       (assoc-in db [:docs id] (transact doc fun-args))
       (do (error ":doc/exec document not found:" id)
           db))
     (do (error ":doc/exec no active document.")
         db))))

(defn exec [fun-args]
  (rf/dispatch [:doc/exec fun-args]))

(swap! fns-lookup assoc
       ;:new-notebook (fn [_] ; doc-old
       ;                (nb/new-notebook))
       ;:load-notebook (fn [_ nb-new] ; doc-old
       ;          (info "loading notebook: " nb-new)
       ;                 (rf/dispatch [:doc/add nb-new])
       ;                 (rf/dispatch [:doc/doc-active (:id data/document)])
       ;            nb-new)
       ;:set-active (fn [doc id]
       ;              (assoc-in doc [:active] id))
       :eval-all (partial eval/eval-all exec)
       :eval-segment (partial eval/eval-segment-id exec)
       :remove-segment-active pos/remove-active-segment
       :insert-before pos/insert-before
       :insert-below pos/insert-below
       :kernel-toggle-active k/kernel-toggle-active
       :kernel-toggle k/kernel-toggle)


;; compatibility


(rf/reg-sub
 :document/current
 (fn [db _]
   (:doc-active db)))

(rf/reg-sub
 :notebook
 (fn [db _]
   (let [id (:doc-active db)]
     (get-in db [:docs id]))))

(rf/reg-sub
 :notebook/queued
 (fn [db _]
   []))

#_(reg-sub
   :notebook/queued  ; all queued segments in current notebook
   :<- [:notebook]
   (fn [notebook _]
     (get-in notebook [:queued])))

(rf/reg-sub
 :segment/queued? ; is seg-id queued ?
 (fn [_ seg-id]
   [(rf/subscribe [:notebook/queued])]) ; reuse subscription :notebook/queued
 (fn [[queued] [_ seg-id]]
   ;(info "queued: " queued)
   (some #(= seg-id %) queued)))

(rf/reg-sub
 :notebook/edit?
 (fn [db [_]]
   (or (:notebook/edit? db) false)))

(rf/reg-sub
 :notebook/segment
 :<- [:notebook]
 (fn [notebook [_ seg-id]]
   (debug "sub seg-id: " seg-id)
   (nb/get-segment notebook seg-id)))







