(ns picasso.kernel.edn
  (:require
   [clojure.edn :as edn]
   #?(:clj  [clojure.core.async :refer [>! <! chan close! go timeout]]
      :cljs [cljs.core.async :refer [>! <! chan close! timeout] :refer-macros [go]])
   [taoensso.timbre :as timbre :refer [debug debugf info error]]
   [picasso.id :refer [guuid]]
   [picasso.kernel.protocol :refer [kernel-eval]]
   [picasso.converter :refer [->picasso]]))

(defmethod kernel-eval :edn [{:keys [id code]
                              :or {id (guuid)}}]
  (let [c (chan)]
    (debug "edn-eval: " code)
    (go (try (let [eval-result (edn/read-string code)
                   _ (<! (timeout 3000)) ; to test eval all
                   _ (debug "eval result: " eval-result)
                   picasso (->picasso eval-result)
                   _ (debug "picasso: " picasso)
              ;(into [] (map ->picasso eval-results))
                   ]
               (>! c {:id id
                      :picasso picasso}))

             (catch #?(:cljs js/Error :clj Exception) e
               (error "edn eval ex: " e)
               (>! c {:id id
                      :error e})))
        (close! c))
    c))




