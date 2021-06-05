(ns demo.events
  (:require
   [taoensso.timbre :as timbre :refer [info warn]]
   [re-frame.core :refer [reg-event-db dispatch]]
   [notebook.template.core :refer [make-notebook]]
   ))

(reg-event-db
 :demo/start
 (fn [db [_]]
   (info "picasso demo started.")
   (dispatch [:webly/status :running])
   db))

(reg-event-db
 :notebook/template
 (fn [db [_]]
   (info "making template.")
   (let [nb (make-notebook)]
   (dispatch [:doc/load nb])
   db)))



