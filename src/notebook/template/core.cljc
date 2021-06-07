(ns notebook.template.core
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
       (add-md "# Pink Gorilla")
       (add-code
        "(ns " hip-nsname ")\n"
        "(require '[pinkgorilla.repl :refer [require-default]]) \n "
        "(require '[pinkgorilla.repl.clj.pomegranate :refer [add-dependencies]]) \n "
        "; (add-dependencies '[cljs-ajax/cljs-ajax \"0.8.3\"]) \n"
        "(require-default)")

       (add-code
        "^:R [:p.bg-blue-300 \"Hello, World!\"] \n")

       (add-code
        "(plot/list-plot (concat (range 10) (reverse (range 10))))"))))
