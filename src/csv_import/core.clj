(ns csv-import.core
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [clojure.java.jdbc :as sql])
  (:gen-class))

(def filepath "./data/direct_mail.promocodes.csv")

(def db {:subprotocol "postgresql"
         :subname "//localhost:5432/clojure"
         :user "john.skilbeck"})

(defn query-db [conn]
  (println
    (sql/query conn ["select * from stuff"])))

(defn insert-db [conn data]
  (sql/insert! conn
              :promocodes {:data data}))

(defn parse [f]
  (csv/parse-csv f))

(defn open-file [fname]
  (with-open [f (io/reader fname)]
    (println 
      (parse f))))

(defn -main [& args]
  (insert-db db (open-file filepath)))

;;(defn -main [& args]
;;  (query-db db))

;;(defn -main [& args]
;;  (open-file filepath))

