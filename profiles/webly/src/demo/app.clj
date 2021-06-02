(ns demo.app
  (:require
   [webly.user.app.app :refer [webly-run!]]
   ; side-effects
   [demo.routes]
   [picasso.kernel.edn]
   [picasso.kernel.clj]))

(defn -main
  [profile-name]
  (webly-run! profile-name "picasso-webly.edn"))

