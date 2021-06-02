(ns picasso.paint.hiccup
  (:require

   [picasso.protocols :refer [paint]]))

(defmethod paint :hiccup [{:keys [content]}]
  ; assumes that reagent renders hiccup
  ; which is correct, except that it does not support string-styles
  content)
