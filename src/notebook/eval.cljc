(ns notebook.eval
  (:require
   [picasso.id :refer [guuid]]
   #?(:clj  [clojure.core.async :refer [<! go go-loop]]
      :cljs [cljs.core.async :refer [<!] :refer-macros [go go-loop]])
   [taoensso.timbre :refer [trace debug info error]]
   #?(:cljs [re-frame.core :as rf])
   [picasso.kernel.protocol :refer [kernel-eval]]
   [notebook.core :as edit]
   [notebook.position :as pos]))

(defn eval-segment [exec {:keys [id data] :as seg}]
  (info "eval seg: " seg)
  (let [eval-id (guuid)
        ev (assoc data :id eval-id)]
    ;(info "eval data: " ev)
    (exec [:clear-segment id])
    (go (let [er (<! (kernel-eval ev))]
          (info "eval-result id: " id "er: " er)
          (exec [:set-state-segment id er])))))

(defn eval-segment-id [exec {:keys [segments] :as doc} id]
  (if-let [seg (edit/get-segment doc id)]
    (eval-segment exec seg)
    (error "segment not found: " id))
  doc)

(defn eval-segment-active [exec doc]
  (if-let [id (pos/active-segment-id doc)]
    (eval-segment-id exec doc id)
    (error "no active segment to evaluate"))
  doc)

; notify of queued segments

#?(:cljs
   (rf/reg-event-db
    :transactor/segment
    (fn [db [_ id]]
      (assoc db :transactor/queued id))))

(defn eval-all [exec {:keys [segments] :as doc}]
  (let [update-status (fn [seg]
                        #?(:cljs (rf/dispatch [:transactor/segment (:id seg)])))
        code-segments (->> segments
                           (filter #(= :code (:type %))))]
    (go-loop [seg (first code-segments)
              code-segments (rest code-segments)]
      (debug "eval all. count: " (count code-segments))
      (if seg
        (do (debug "evaluating next segment: " (:id seg))
            (update-status seg)
            (<! (eval-segment exec seg))
            (recur (first code-segments) (rest code-segments)))
        (do (debug "eval all finished")
            (update-status seg))))
    (edit/clear-all doc)))