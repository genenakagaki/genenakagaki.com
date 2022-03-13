(ns web.components.button
  (:require
   [clojure.string :as str]
   [reagent.core :as r]
   [malli.core :as m]
   [web.shared :refer [validate Component]]
   [web.components.icon :as icon]))


#_(defn m-button []
  (fn [props]
    (validate [:map
               :child (m/form Component)])))

#_(defn m-nav-button []
  (fn [props]
    (validate [:map
               :child (m/form Component)])))

(def Fab
  [:map
   [:icon :string]
   [:on-click {:optional true} fn?]])

(defn m-fab [props]
  (validate Fab props)
  (let [icon (:icon props)
        on-click (:on-click props)]
    [:button.fixed.right-4.bottom-4.rounded-full.bg-secondary.text-on-secondary
     {:on-click (when (nil? on-click) on-click)}
     [:div.m-4
      [icon/icon {:value icon}]]]))
