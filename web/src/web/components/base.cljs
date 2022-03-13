(ns web.components.base
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [malli.core :as m]
            [malli.transform :as mt]
            [malli.util :as mu]
            [reitit.frontend.easy :as rfe]
            [web.shared :refer [validate]]
            [web.components.list :as list]
            ))

(defn m-list [props]
  (validate [:map
             [:items [:* (mu/merge list/Row list/ItemContent)]]]
            props)
  (fn [{:keys [items]}]
    [:ul.w-full.py-2.border-gray-200.border.list-none
     (for [item items] 
       (let [transformer (mt/transformer mt/strip-extra-keys-transformer
                                         mt/default-value-transformer)
             row-props (m/decode list/Row item transformer)
             item-content-props (m/decode list/ItemContent item transformer)]
         ^{:key item}
         [list/row row-props 
          [list/item-content item-content-props]]))]))

(defn m-navigation-drawer [props]
  (validate [:map
             [:items any?]
             [:open? boolean?]]
            props)
  (fn [{:keys [open? items]} props]
    [:div.relative
     [:div.w-full.absolute.top-0.bg-white.transition-all.ease-out.duration-300
      {:class (if open?
                "left-0"
                "-left-full")}
      [:span.absolute.block.transition-all.ease-out.duration-1000.delay-500.text-xs
       {:class (if open?
                 "left-0"
                 "-left-full")}
       "|･ω･`)ﾉ "]
      [m-list {:items items}]]]))

(defn m-app-bar [props]
  (let [open? (r/atom false)]
    (fn []
      [:div#m-app-bar
       [:div.h-16.px-4.flex.items-center.bg-primary.text-on-primary
        [:button.pr-8
         [:span.material-icons
          {:on-click #(swap! open? not)}
          "menu"]]
        [:a.font-mono.text-xl.block
         {:href "/"}
         "Gene N: Blog"]]
       [m-navigation-drawer {:open? @open?
                             :items [{:icon "home"
                                      :text "Home"
                                      :on-click #(rfe/push-state :web.router/home)}
                                     {:icon "list"
                                      :text "Logs"
                                      :on-click #(rfe/push-state :web.router/logs) }
                                     {:icon "menu_book"
                                      :text "Recipes"
                                      :href "/recipes"}
                                     {:icon "restaurant"
                                      :text "Diets"
                                      :href "/diets"}
                                     {:icon "grass"
                                      :text "Food"
                                      :href "/food"}]}]]
      )))
