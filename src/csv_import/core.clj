(ns csv-import.core
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [clojure.java.jdbc :as sql]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.format :as f])
  (:gen-class))

(def filepath "./data/direct_mail.promocodes22.csv")

(def db {:subprotocol "postgresql"
         :subname "//docker:5432/direct_mail"
         :user "john.skilbeck"})

(defn parse-int [s]
  (Integer. (re-find #"\d+" s)))

(defn alter-map [m fnmap]
  (reduce (fn [m [k f]]
    (assoc m k (f (m k))))
      m fnmap))

(defn transform [record]
  (alter-map record {(keyword "\"Mail Date\"") c/to-sql-date,
                     (keyword "\"Mailed Quantity\"") parse-int,
                     (keyword "\"Actual Mailed Quantity\"") parse-int}))

(defn insert-db [record]
  (sql/insert! db :promocodes2 (transform record)))

(defn parse-row [row]
  (let [v (first(csv/parse-csv row))]
    (zipmap 
      [:model :datasource 
       (keyword "\"Add Suppresions\"") (keyword "\"Selection Strategy Notes\"") 
       (keyword "\"New Remail\"") (keyword "\"Mail Date\"") (keyword "\"Mailed Quantity\"") (keyword "\"Actual Mailed Quantity\"")
      ] v)))

(defn read-file [fname]
  (with-open [rdr (io/reader fname)]
    (doseq [line (line-seq rdr)]
      (->
        line
        parse-row
        insert-db
        ))))

(defn -main [& args]
  (->
    filepath
    read-file
    ))

