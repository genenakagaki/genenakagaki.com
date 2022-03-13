(ns web.components.list
  (:require
   [clojure.string :as str]
   [reagent.core :as r]
   [malli.core :as m]
   [web.shared :refer [validate Component]]
   [web.components.icon :as icon]))

(def ItemContent
  [:map 
   [:icon {:optional true} string?]
   [:text string?]
   [:secondary-text {:optional true} string?]
   [:tertiary-text {:optional true} string?]])

(defn item-content [props]
  (validate ItemContent props)
  (let [{:keys [icon text]} props]
    [:div.h-12.w-full.px-4.flex.items-center
     (when-not (nil? icon)
       [:div.mr-8
        [icon/icon {:value icon :size :md}]])
     [:span.text-base text]]))

(def Row
  [:map
   [:on-click {:optional true} fn?]])

(defn row [props child]
  (validate Row props)
  (let [{:keys [on-click]} props]
    [:li.hover:bg-primary-light.active:bg-primary
     (if (nil? on-click)
       child
       [:button.w-full {:on-click on-click}
        child]
       )]))

