(ns ui.notebook.css
  (:require
   [re-frame.core :as rf]))

(def components
  {:datatypes {true ["picasso/datatypes.css"]}})

(def config
  {:datatypes true ; picasso clj/cljs datatypes 
   })

(rf/dispatch [:css/add-components components config])
