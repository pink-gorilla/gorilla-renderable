(ns notebook.core
  (:require
   [picasso.id :refer [guuid]]
   [com.rpl.specter :as s]))

; helper fns

(defn seg-with-id [target-id {:keys [id] :as segment}]
  (= id target-id))

(defn seg-with-type [target-type {:keys [type] :as segment}]
  (= type target-type))

(defn seg-code [{:keys [type] :as segment}]
  (= type :code))

; notebook

(defn new-notebook []
  {:meta {:id (guuid)}
   :segments []})

(defn set-meta-key [doc k v]
  (assoc-in doc [:meta k] v))

(defn md-segment [md]
  {:id (guuid)
   :type :md
   :data (or md "")
   :state nil})

(defn code-segment [kernel code]
  {:id (guuid)
   :type :code
   :data   {:kernel (or kernel :clj)
            :code (or code "")}
   :state {}})

(defn add-segments [doc segs]
  (let [segs (into []
                   (concat (:segments doc) segs))]
    (assoc doc :segments segs)))

(defn add-segment [doc segment]
  (add-segments doc [segment]))

(defn add-md [doc md]
  (->> (md-segment md) (add-segment doc)))

(defn add-code [doc kernel code]
  (->> (code-segment kernel code) (add-segment doc)))

(defn clear-segment [doc id]
  (s/setval
   [:segments (s/filterer (partial seg-with-id id)) s/ALL :state]
   {} ; s/NONE
   doc))

(defn clear-all [{:keys [segments] :as doc}]
  (->> (map #(assoc % :state {}) segments)
       (into [])
       (assoc doc :segments)))

;(defn update-segment-state [{:keys [segments] :as doc} seg-id])

(defn remove-segment [doc id]
  (s/transform
   [:segments (s/filterer (partial seg-with-id id))]
   s/NONE
   doc))

(defn set-code-segment [doc id code]
  (s/setval
   [:segments (s/filterer (partial seg-with-id id)) s/ALL :data :code]
   code
   doc))

(defn insert-before [doc idx seg]
  (s/setval
   [:segments (s/before-index idx)]
   seg
   doc))

(defn set-md-segment [doc id md]
  (s/setval
   [:segments (s/filterer (partial seg-with-id id)) s/ALL :data]
   md
   doc))

(defn get-segment [doc id]
  (s/select-first
   [:segments (s/filterer (partial seg-with-id id)) s/FIRST]
   doc))

(defn set-kernel-segment [doc id kernel]
  (s/setval
   [:segments (s/filterer (partial seg-with-id id)) s/ALL :data :kernel]
   kernel
   doc))

(defn set-state-segment [doc id state]
  (s/setval
   [:segments (s/filterer (partial seg-with-id id)) s/ALL :state]
   state
   doc))

(defn toggle-type-segment
  [doc {:keys [id type] :as segment}]
  (let [segment (if (= type :code)
                  (md-segment (get-in  segment [:data :code]))
                  (code-segment :clj (:data segment)))
        segment (assoc segment :id id)]
    (s/setval
     [:segments (s/filterer (partial seg-with-id id)) s/ALL]
     segment
     doc)))

(comment

  (s/setval [:a (s/compact :b :c)] s/NONE {:a {:b {:c 1}}})

  (s/setval [:a :b :c] s/NONE {:a {:b {:c 1}}})

  (require '[picasso.data.document])

  (get-segment picasso.data.document/document 2)
  (remove-segment picasso.data.document/document 3)
  (clear-segment picasso.data.document/document 2)
  (set-code-segment picasso.data.document/document 2 "(+ 99 77 55)")
  (set-state-segment picasso.data.document/document 2 {:picasso [:p 7]})
  (s/transform [:segments (s/filterer (partial seg-with-id 2))] s/NONE
               picasso.data.document/document)

  (s/setval [s/INDEXED-VALS s/FIRST] 1 [5 6 7 8 9])

  (s/select [s/INDEXED-VALS ;s/FIRST 
             (s/filterer (fn [k] (= k 3)))
           ;s/FIRST 
             ]
            [5 6 7 8 9])

  (s/setval [s/INDEXED-VALS
           ;s/FIRST 
             (s/filterer (fn [k] (= k 2)))
             s/FIRST]
            3
            [5 6 7 8 9])

  (s/setval (s/index-nav 2) 0 [1 2 3 4 5])

  ;
  )