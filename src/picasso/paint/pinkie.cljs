(ns picasso.paint.pinkie
  (:require
   [picasso.protocols :refer [paint make]]
   [pinkie.pinkie-render :refer [pinkie-render]]))

(defmethod paint :pinkie [{:keys [#_type content]}]
  [pinkie-render {:map-keywords true
                  :fix-style true} content])

(defmethod paint :reagent [{:keys [#_type content]}]
  (let [{:keys [hiccup map-keywords]} content]
    (if map-keywords
      (do ;(debug "rendering reagent with pinkie..")
        (paint (make :pinkie hiccup)))
      (do ;(debug "rendering reagent direct ..")
        hiccup))))
