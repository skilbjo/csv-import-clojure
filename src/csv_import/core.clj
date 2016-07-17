(ns csv-import.core
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [clojure.java.jdbc :as sql])
  (:gen-class))

(def filepath "./data/direct_mail.promocodes22.csv")

(def db {:subprotocol "postgresql"
         :subname "//docker:5432/direct_mail"
         :user "john.skilbeck"})

; make this a vector of maps
(defn insert-db 
  [record]
  (println [record])
  (sql/insert! db
    :promocodes2 [record]))
;  (let [row (into [] (map record))]
;    (println row)
;    (sql/insert! db
;      :promocodes2 row)))
    

(defn parse-row [row]
  (let [v (first(csv/parse-csv row))]
    (zipmap [:model 
            :datasource 
            (keyword "\"Add Suppresions\"")
            (keyword "\"Selection Strategy Notes\"") 
            (keyword "\"New/Remail\"")
            (keyword "\"Mail Date\"")
            (keyword "\"Mailed Quantity\"")
            (keyword "\"Actual Mailed Quantity\"")
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

