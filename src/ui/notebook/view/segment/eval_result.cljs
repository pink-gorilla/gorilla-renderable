(ns ui.notebook.view.segment.eval-result
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :as rf :refer [subscribe dispatch]]
   [picasso.kernel.view.eval-result :refer [eval-result-view]]))

(defn er-segment
  [nb-settings {:keys [id state] :as seg}]
  (if state
    [eval-result-view state]
    [:div "no eval result"]))

