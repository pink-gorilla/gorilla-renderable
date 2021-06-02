(ns picasso.paint.html-text
  (:require
   [taoensso.timbre :refer-macros [debug]]
   [picasso.protocols :refer [paint make]]
   [pinkie.html :refer [html]]
   [pinkie.text :refer [text]]))

(defmethod paint :html [{:keys [#_type content]}]
  [html content])

(defmethod paint :text [{:keys [#_type content]}]
  [text content])