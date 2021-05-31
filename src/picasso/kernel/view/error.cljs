(ns picasso.kernel.view.error
  (:require
   [pinkie.text :refer [text]]
   [reepl.helpers :as helpers]))

(def styles
  {:error-text {:font-family "monospace"
                :border-style "solid"
                :border-width "1px"
                :border-color "#ff0000"
                :color "#ff0000"
                :clear "both"
                :padding "0.5em 1em 0.5em 1em"
                :margin-bottom "0.3em"
               ;background-color "#faf0f2"
                }})

(def view (partial helpers/view styles))

(defn err-text? [err]
  (if (nil? err)
    false
    (string? err)))

(defn err-vec? [err]
  (if (nil? err)
    false
    (and (vector? err) (not (empty? err)))))

(defn error-text [err root-ex]
  (let [err-vec (err-vec? err)
        err-text (err-text? err)]
    (when (or err-text err-vec root-ex)
      [view :error-text
       (when err-text
         [text err])
       (when err-vec
         nil ; todo - when does this happen¿
         )
       (when root-ex
         [text root-ex])])))