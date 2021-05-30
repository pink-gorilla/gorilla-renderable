(ns demo.kernel
  (:require
   [clojure.core.async :refer [<! <!! go]]
   [taoensso.timbre :as timbre :refer [debugf info error]]
   [picasso.default-config] ; side-effects
   [picasso.kernel.protocol :refer [kernel-eval]]
   ))

(defn eval-clj [code]
  (go
    (let [er  (<! (kernel-eval {:kernel :clj :code code}))]
      (info "er:" er))))

(eval-clj "13")

(eval-clj "(+ 1 1) [7 8]")
