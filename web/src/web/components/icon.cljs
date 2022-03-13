(ns web.components.icon
  (:require [web.shared :refer [validate]]
            [malli.core :as m]
            [malli.transform :as mt]))

(def Icon
  [:map
   [:value :string]
   [:size {:default :md} [:enum :sm :md :lg]]])

(defn icon [props]
  (let [icon-props (m/decode Icon props mt/default-value-transformer)]
    (validate Icon icon-props)
    [:span.material-icons.icon.text-gray-600
     {:class (case (:size icon-props)
               :sm "icon-sm"
               :md "icon-md"
               :lg "icon-lg")}
     (:value icon-props)]))

