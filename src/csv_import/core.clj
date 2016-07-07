(ns csv-import.core
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [clojure.java.jdbc :as sql])
  (:gen-class))

(def filepath "./data/direct_mail.promocodes2.csv")

(def db {:subprotocol "postgresql"
         :subname "//localhost:5432/clojure"
         :user "john.skilbeck"})

(defn insert-db [row]
  (sql/insert! db 
              :promocodes2 {:model row}))

(defn parse-row [row]
  (first
    (first
      (csv/parse-csv row))))


;(defn parse-row [row]
;  (let [v (first (csv/parse-csv row))]
;    (zipmap [:model] v)))

(defn read-file [fname]
  (with-open [rdr (io/reader fname)]
    (doseq [line (line-seq rdr)]
      (insert-db
        (parse-row line)))))

(defn -main [& args]
  (->
    filepath
    read-file))

