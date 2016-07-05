(ns solsort.www
  (:use [hiccup.page :only (html5)])
  (:require [clojure.string :as str])
  )
(def apps-list
  (rest
   [nil
    [:git 2016  7 "solsort/www"]
    [:git 2016  7 "solsort/fmtools"]
    [:web 2016  5 "https://openplatform.dbc.dk" "Den Åbne Platform"]
    [:wordpress 2016  5 "alive.solsort.com"]
    [:web 2016  4 "https://forum.tinkuy.dk" "Tinkuy Forum"]
    [:git 2016  1 "rasmuserik/mubackend"]
    [:git 2016  1 "rasmuserik/apps"]
    [:wordpress 2016  0 "rasmuserik.com"]
    [:wordpress 2016  0 "annevoel.dk"]
    [:git 2016  0 "solsort/mobibl"]
    [:git 2015 12 "rasmuserik/html5book"]
    [:git 2015 12 "rasmuserik/app-list"]
    [:git 2015 11 "rasmuserik/bibapp"]
    [:git 2015  9 "NewCircleMovement/lemon"]
    [:git 2014 12 "solsort/visualisering-af-relationer"]
    [:git 2014  4 "rasmuserik/sketch-note-draw"]
    [:git 2014  3 "rasmuserik/single-touch-snake"]
    [:git 2014  3 "rasmuserik/morse-code"]
    [:git 2014  3 "rasmuserik/frie-sange"]
    [:git 2014  2 "rasmuserik/kbh-parking"]
    [:git 2014  2 "OneTwo360/360-viewer"]
    [:git 2013  9 "rasmuserik/art-quiz"]
    [:git 2013  4 "rasmuserik/app-tsartnoc"]
    [:git 2012  5 "rasmuserik/blobshot"]
    [:git 2011  8 "rasmuserik/planetcute"]
    [:git 2011  8 "rasmuserik/dkcities"]
    [:git 2011  3 "rasmuserik/notescore"]
    [:git 2011  3 "rasmuserik/julia4d"]
    [:git 2011  3 "rasmuserik/js1k-sierpinsky"]
    [:git 2011  3 "rasmuserik/js1k-rain"]
    [:git 2011  3 "rasmuserik/js1k-brownian"]
    [:git 2011  0 "rasmuserik/timelog"]
    [:git 2011  0 "rasmuserik/combigame"]]
   ))

(def month-names
  ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"])
(defn git [id]
  (let [short-name (second (str/split id #"/"))
        url (str "https://" short-name ".solsort.com")]
    {:url url
     :icon (str url "/icon.png")})
  )
(defn web [id title]
  {:url id :title title})
(defn wp [id]
  {:url (str "https://" id)})

(defn app-to-obj [[type year month & args]]
  (print 'here)
  (into {:type type
          :year year
         :month (get month-names month)
         :args args}
        (apply ({:git git
                 :web web
                 :wordpress wp} type) args)))

(defn entry [o]
  [:div.entry
   [:img.icon {:src (:icon o)}]
   [:div.date (:month o) (:year o)]
   [:div.title (:title o)]])

(->> apps-list
     (map app-to-obj)
     )

(println (map app-to-obj apps-list))

(defn htmldoc []
  (html5 
   [:head [:title "solsort.com"]]
   [:body
    [:h1 "hello"]
    (into [:div]
          (->> apps-list
               (map app-to-obj)
               (map entry)))
    ]))
(println (htmldoc))

(defn -main [& args]
  (println 'main (html5 [:h1 "hello"]) args))

