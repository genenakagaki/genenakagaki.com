(ns web.router
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as rf]
   [reitit.coercion.spec :as reitit-spec]
   [reitit.frontend :as reitit]
   [reitit.frontend.easy :as rfe]))

(defn home-view []
  [:div [:h1 "Home"]])

(defn blog-view []
  [:div [:h1 (str "blog: " @(rf/subscribe [::route-path-params :slug]))]])

(defn not-found-view []
  [:div [:h1 "404"]])

(def router
  (reitit/router
   [["/" {:name ::home
          :view home-view}]
    ["/blog/:slug" {:name :blog
                    :view blog-view}]
    ["/404" {:name :not-found
             :view not-found-view}]]
   {:data {:coercion reitit-spec/coercion}}))

(defn ^:private store-route [db matched-route]
  (print matched-route)
  (assoc db ::route {:name (get-in matched-route [:data :name])
                     :path-params (:path-params matched-route)
                     :query-params (:query-params matched-route)}))
(comment
  (s/fdef store-route
    :args (s/cat :db any?
                 :matched-route ::matched-route))
  (stest/instrument `store-route))

(rf/reg-event-db
 ::navigate
 (fn-traced [db [_ matched-route]]
            (if (nil? matched-route)
              (store-route db (reitit/match-by-name router :not-found))
              (store-route db matched-route))))

(rf/reg-sub
 ::route
 (fn [db query]
   (::route db)))

(rf/reg-sub
 ::route-view
 (fn [query]
   (rf/subscribe [::route]))
 (fn [{route-name :name}
      query]
   (let [matched-route (reitit/match-by-name router route-name)
         view-fn (get-in matched-route [:data :view])]
     (when-not (nil? view-fn)
       (view-fn)))))

(rf/reg-sub
 ::route-path-params
 (fn [query]
   (rf/subscribe [::route]))
 (fn [{path-params :path-params}
      [_ key]]
   (get path-params key)))

(rf/reg-sub
 ::route-query-params
 (fn [query]
   (rf/subscribe [::route]))
 (fn [{query-params :query-params}
      [_ key]]
   (get query-params key)))
