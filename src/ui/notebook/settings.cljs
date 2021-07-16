(ns ui.notebook.settings
  (:require
   [taoensso.timbre :as timbre :refer [debug info infof warn error]]
   [re-frame.core :as rf]))

(def default-notebook-settings
  {:layout :vertical
   :view-only false})

(rf/reg-sub
 :notebook/settings
 :<- [:settings] ; defined in webly
 (fn [settings _]
   (or (:notebook settings) default-notebook-settings)))

(rf/reg-event-fx
 :notebook/setting-set
 (fn [{:keys [db]} [_ kw-option layout]]
   (let [nb-settings-old (or (get-in db [:settings :notebook]) default-notebook-settings)
         nb-settings-new (assoc nb-settings-old kw-option layout)]
     (info "new nb settings: " nb-settings-new)
     (rf/dispatch [:settings/set :notebook nb-settings-new]))))

(def layouts
  [:single :horizontal :vertical :stacked])

(rf/reg-event-db
 :notebook/layout-toggle
 (fn [db _]
   (let [layout (or (get-in db [:settings :notebook :layout]) (:layout default-notebook-settings))

         _ (error "settings: " (get-in db [:settings]))
         v-indexed (map-indexed (fn [idx id] [idx id]) layouts)
         current (first
                  (filter
                   (fn [[idx id]] (= layout id))
                   v-indexed))
         current-idx (get current 0)
         new-idx (if (= current-idx 0)
                   (- (count layouts) 1)
                   (- current-idx 1))
         layout-new (get layouts new-idx)]
     (infof "changing layout %s -> %s" layout layout-new)
     (rf/dispatch [:notebook/setting-set :layout layout-new])
     db)))