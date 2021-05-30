(ns notebook.template
  "Create a new notebook:
   Take a sample notebook, and give it a hipster namespace name"
  (:require
   [notebook.core :as nb]
   [notebook.template.hipster :refer [make-hip-nsname]]))

(defn add-md [notebook & md]
  (if md (nb/add-md notebook (apply str md))
      notebook))

(defn add-code [notebook & code]
  (nb/add-code notebook :clj (apply str code)))

(defn snippets->notebook
  ([snippets]
   (reduce add-code (nb/new-notebook) snippets))
  ([snippets md]
   (let [nb  (-> (nb/new-notebook)
                 (add-md md))]
     (reduce add-code nb snippets))))

(defn make-notebook
  "A pure function that creates a new worksheet in the browser.
  All db functions used are pure functions!"
  ([]
   (make-notebook (make-hip-nsname)))
  ([hip-nsname]
   (-> (nb/new-notebook)
       (add-md "# Pink Gorilla \n\n"
               "Shift + enter evaluates code. "
               "Hit CTRL+g twice in quick succession or click the menu icon (upper-right corner) for more commands.\n\n"
               "It's a good habit to run each worksheet in its own namespace. We created a random namespace for you; you can keep using it.")
       (add-code
        "; Automatically Download Dependencies (if they are not installed already) \n "
        "(use '[pinkgorilla.notebook.repl]) \n "
        "(pinkgorilla.notebook.repl/add-dependencies '[org.pinkgorilla/gorilla-plot \"0.9.3\"])")

       (add-code
        "; Define Namespace for your notebook and require namespaces \n"
        "(ns " hip-nsname "  \n"
        "  (:require \n"
        "     [pinkgorilla.ui.gorilla-plot.core :refer [list-plot bar-chart compose histogram plot]] \n"
        "     ))\n")

       (add-code
        "^:R \n"
        "  [:<> \n"
        "    [:h4 \"Hello, World!\"] \n"
        "    [:div {:style {:display :flex :flex-direction :row\n"
        "                   :color :green :font-weight :bold :background-color :pink \n"
        "                   :margin-left 20 :padding 10 :border :solid :width 500}} \n"
        "      [:ol \n"
        "        [:li \"The Pinkie\"] \n"
        "        [:li \"The Pinkie and the Brain\"]  \n"
        "        [:li \"What will we be doing today?\"]]  \n"
        "      [:img {:height 100 :width 100 \n"
        "             :src \"https://images-na.ssl-images-amazon.com/images/I/61LeuO%2Bj0xL._SL1500_.jpg\"}]]]")

       (add-code
        " ^:R [:p/vega \"https://raw.githubusercontent.com/vega/vega/master/docs/examples/bar-chart.vg.json\" ]")

       (add-code
        "(list-plot (concat (range 10) (reverse (range 10))))"))))
