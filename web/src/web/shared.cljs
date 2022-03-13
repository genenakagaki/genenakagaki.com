(ns web.shared
  (:require
   [malli.core :as m]
   [malli.error :as me]))

(def Component [:any])

(defn validate [schema data]
  (when-not (m/validate schema data)
    (let [error (-> (m/explain schema data)
                    #_(me/humanize))]
      (throw (ex-info
              "Schema validation failed"
              error)))))
