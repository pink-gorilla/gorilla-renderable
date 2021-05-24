(ns picasso.document.index)

#_(defn segments-ordered [notebook]
    (let [segments (:segments notebook)
          segment-ids-ordered (:order notebook)]
      (vec (map #(get segments %) segment-ids-ordered))))

#_(defn insert-segment-at
    [notebook new-index new-segment]
    (let [{:keys [order active segments]} notebook
          new-id (:id new-segment)
          [head tail] (split-at new-index order)]
      (merge notebook {:active new-id
                       :segments       (assoc segments new-id new-segment)
                       :order  (into [] (concat head (conj tail new-id)))})))

#_(defn insert-segment-bottom
    [notebook new-segment]
    (let [{:keys [order active segments]} notebook
          new-id (:id new-segment)]
      (merge notebook {:active new-id
                       :segments (assoc segments new-id new-segment)
                       :order  (into [] (conj order new-id))})))