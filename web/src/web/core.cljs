(ns web.core
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as rf]
   [reagent.dom :as rdom]
   [reitit.frontend.easy :as reitit-frontend-easy]
   [web.components.base :as base]
   [web.config :as config]
   [web.router :as router]
   [web.views :as views]))

(defn dev-setup []
  (when config/debug?
    (s/check-asserts true)
    (println "dev mode")))

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _] {}))

(defn main-panel []
  [:div
   [base/m-app-bar]
   [:main.container
    @(rf/subscribe [::router/route-view])]])

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))

(defn on-navigate [matched-route]
  (print matched-route)
  (rf/dispatch [::router/navigate matched-route])
  (mount-root))

(defn init-routing! []
  (reitit-frontend-easy/start!
   router/router
   on-navigate
   ;; set to false to enable HistoryAPI
   {:use-fragment false}))

(defn init! []
  (rf/dispatch-sync [::initialize-db])
  (init-routing!)
  (dev-setup))
