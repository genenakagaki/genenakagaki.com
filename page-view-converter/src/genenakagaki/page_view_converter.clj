(ns genenakagaki.page-view-converter
  (:gen-class)
  (:require
   [hickory.core :as h]
   [hickory.select :as hs]
   [hickory.convert :as hc]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn files [directory-path]
  (let [files-and-directory (-> directory-path
                                (io/file)
                                (file-seq))]
    (filter #(.isFile %) files-and-directory)))

(defn directory-format [directory-path]
  (if (= \/ (last directory-path))
    directory-path
    (str directory-path "/")))

(defn output-file-path [file-path original-directory output-directory]
  (-> file-path
      (str/replace (re-pattern (directory-format original-directory))
                   (directory-format output-directory))
      (str/replace #".html$" ".cljs")))

(defn html->hiccup [html]
  (let [hickory-data (-> (h/parse html)
                         (h/as-hickory))]
    (->> hickory-data
         (hs/select (hs/tag :body))
         (first)
         (hc/hickory-to-hiccup))))

(defn convert-pages! [output-directory]
  (let [page-root-directory (-> (io/resource "blog")
                                (.getPath))]
    (->> (files page-root-directory)
         (run! (fn [file]
                 (let [output-file-path (output-file-path (.getPath file)
                                                          page-root-directory
                                                          output-directory)]
                   (io/make-parents output-file-path)
                   (spit output-file-path (-> (slurp file)
                                              (html->hiccup)
                                              (str)))))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (convert-pages! (first args)))

#_(-main "/Users/genenakagaki/genenakagaki.com/output/")

