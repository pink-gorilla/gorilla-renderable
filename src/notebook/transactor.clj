(ns notebook.transactor
  (:require
   [taoensso.timbre :as timbre :refer [debugf info error]]
   [notebook.core :as core]
   [notebook.eval :as eval]
   [notebook.transact :refer [fns-lookup transact]]))

(defonce doc (atom nil))

(defn exec [fun-args]
  (reset! doc (transact @doc fun-args))
  nil)

(swap! fns-lookup assoc
       :new-notebook (fn [_] ; doc-old
                       (core/new-notebook))
       :load-notebook (fn [_ nb-new] ; doc-old
                        (info "loading notebook: " nb-new)
                        nb-new)
       :eval-all (partial eval/eval-all exec)
       :eval-segment (partial eval/eval-segment-id exec))

(defn notebook []
  @doc)

(comment
  (require '[picasso.data.document])
  (reset! doc picasso.data.document/document)
  (exec [:clear-all])
  (exec [:remove-segment 2])
  (exec [:set-state-segment 2 {:bongo :trott}])
  (exec [:eval-all])
  (exec [:eval-segment 2])

  @doc


 ; 
  )