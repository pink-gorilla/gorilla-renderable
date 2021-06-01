(ns notebook.position
  (:require
   [taoensso.timbre :as timbre :refer [debugf info error]]
   [re-frame.core :as rf]
   [notebook.core :as nb]
   [com.rpl.specter :as s]))

(defn active-segment-id [notebook]
  (:active notebook))

(defn remove-active-segment [notebook]
  (nb/remove-segment notebook (active-segment-id notebook)))

(defn segment-active [doc]
  (let [active-id (:active doc)
        segment (when (and doc active-id)
                  (nb/get-segment doc active-id))]
    segment))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))

(defn position [pred coll]
  (first (positions pred coll)))

(defn segment-index [doc seg-id]
  (first (positions #(= seg-id (:id %)) (:segments doc))))

(defn insert-before [doc]
  (let [active-id (:active doc)
        active-idx (when active-id (segment-index doc  active-id))]
    (info "insert-before id:" active-id "pos: " active-idx)
    (if active-idx
      (nb/insert-before doc active-idx (nb/code-segment :clj ""))
      doc)))

(defn insert-below [doc]
  (let [active-id (:active doc)
        active-idx (when active-id (segment-index doc  active-id))]
    (info "insert-below id:" active-id "pos: " active-idx)
    (if active-idx
      (nb/insert-before doc (inc active-idx) (nb/code-segment :clj ""))
      doc)))

(defn move-up-down [doc direction]
  (if-let [current-segment-id (:active doc)]
    (let [_ (info "move-up-down " current-segment-id "dir:" direction)
          segments (:segments doc)
          order (map :id segments)
          v-indexed (map-indexed (fn [idx id] [idx id]) order)
        ;_ (info "v-indexed: " v-indexed)
          current (first
                   (filter
                    (fn [[idx id]] (= current-segment-id id))
                    v-indexed))
        ;_ (info "current: " current)
          current-idx (get current 0)
          lookup (into {} v-indexed)
        ;_ (info "lookup: " lookup)
          idx-target (case direction
                       :down (min (+ current-idx 1) (- (count segments) 1))
                       :up (max 0 (- current-idx 1)))
        ;_ (info "idx-target: " idx-target)
          ]
      (info "new current: " (get lookup idx-target))
      (get lookup idx-target))
    (do (error "cannot move-up-down: no active segment")
        nil)))
(defn move [doc direction & [id]]
  (let [active-id (case direction
                    :up (move-up-down doc direction)
                    :down (move-up-down doc direction)
                    :first (first (:segments doc))
                    :last (last (:segments doc))
                    :to id)]
    (assoc doc :active active-id)))

(rf/reg-sub
 :notebook/segment-active
 :<- [:notebook/current]
 (fn [notebook _]
   (segment-active notebook)))

; commands

(rf/reg-event-db
 :segment-active/delete
 (fn [db _]
   (info "delete active segmentl")
   (rf/dispatch [:doc/exec [:remove-segment-active]])
   db))


