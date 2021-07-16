(ns ui.notebook.loader.list
  (:require
   [taoensso.timbre :as timbre :refer-macros [debugf info error]]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :notebook-list/set
 (fn [db [_ nb-list]]
   (let [nb-lists (or (get-in db [:notebook/lists]) {})]
     (if-let [name (:name nb-list)]
       (assoc-in db [:notebook/lists] (assoc nb-lists name nb-list))
       db))))

(rf/reg-sub
 :notebook-lists
 (fn [db _]
   (or (get-in db [:notebook/lists]) {})))
