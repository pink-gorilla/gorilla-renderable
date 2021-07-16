(ns ui.notebook.loader.load
  (:require
   [taoensso.timbre :as timbre :refer-macros [debugf info error errorf]]
   [re-frame.core :as rf]
   [notebook.template.core :refer [make-notebook]]))

(defmulti load-notebook (fn [m] (:type m)))

(defmethod load-notebook :default [{:keys [type]}]
  (errorf "cannot load notebook type [%s] - not implemented!"))

(defmethod load-notebook :embedded [{:keys [type data]}]
  (rf/dispatch [:doc/load data]))

(rf/reg-event-db
 :notebook/template
 (fn [db [_]]
   (info "making template.")
   (let [nb (make-notebook)]
     (rf/dispatch [:doc/load nb])
     db)))

(defmethod load-notebook :template [{:keys [type data]}]
  (rf/dispatch [:notebook/template]))