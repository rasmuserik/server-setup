(ns solsort.www.main
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [solsort.misc :refer [<blob-url]]
   [solsort.util
    :refer
    [<p <ajax <seq<! js-seq normalize-css load-style! put!close!
     parse-json-or-nil log page-ready render dom->clj next-tick]]
   [reagent.core :as reagent :refer []]
   [cljs.reader :refer [read-string]]
   [clojure.data :refer [diff]]
   [clojure.string :as string :refer [replace split blank?]]
   [cljs.core.async :as async :refer [>! <! chan put! take! timeout close! pipe]]))

(def month-names
  ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"])
(defn git [y m id]
  (let [short-name (second (string/split id #"/"))
        url (str "https://" short-name ".solsort.com")]
    {:year y
     :month (month-names m)
     :url url
     :icon (str url "/icon.png")})
  )
(defn web [y m id title icon]
  {:year y
   :month (month-names m)
   :url id
   :title title})
(defn wordpress [y m host]
  {:year y
   :month (month-names m)
   :icon "https://s.w.org/about/images/logos/wordpress-logo-notext-rgb.png"
   :type :wordpress
   :url (str "https://" host)})

(def apps
  [(git 2016  7 "solsort/www")
   (git 2016  7 "NewCircleMovement/tinkuy-member-check")
   (git 2016  7 "solsort/fmtools")
   (web 2016  5 "https://openplatform.dbc.dk" "Den Åbne Platform" "/assets/openplatform.png")
   (wordpress 2016  5 "alive.solsort.com")
   (web 2016  4 "https://forum.tinkuy.dk" "Tinkuy Forum" "https://forum.tinkuy.dk/uploads/system/site-logo.jpg")
   (git 2016  1 "rasmuserik/mubackend")
   (git 2016  1 "rasmuserik/apps")
   (wordpress 2016  0 "rasmuserik.com")
   (wordpress 2016  0 "annevoel.dk")
   (git 2016  0 "solsort/mobibl")
   (git 2015 12 "rasmuserik/html5book")
   (git 2015 12 "rasmuserik/app-list")
   (git 2015 11 "rasmuserik/bibapp")
   (git 2015  9 "NewCircleMovement/lemon")
   (git 2014 12 "solsort/visualisering-af-relationer")
   (git 2014  4 "rasmuserik/sketch-note-draw")
   (git 2014  3 "rasmuserik/single-touch-snake")
   (git 2014  3 "rasmuserik/morse-code")
   (git 2014  3 "rasmuserik/frie-sange")
   (git 2014  2 "rasmuserik/kbh-parking")
   (git 2014  2 "OneTwo360/360-viewer")
   (git 2013  9 "rasmuserik/art-quiz")
   (git 2013  4 "rasmuserik/app-tsartnoc")
   (git 2012  5 "rasmuserik/blobshot")
   (git 2011  8 "rasmuserik/planetcute")
   (git 2011  8 "rasmuserik/dkcities")
   (git 2011  3 "rasmuserik/notescore")
   (git 2011  3 "rasmuserik/julia4d")
   (git 2011  3 "rasmuserik/js1k-sierpinsky")
   (git 2011  3 "rasmuserik/js1k-rain")
   (git 2011  3 "rasmuserik/js1k-brownian")
   (git 2011  0 "rasmuserik/timelog")
   (git 2011  0 "rasmuserik/combigame")])

(defn entry [o]
  [:div.entry
   [:img.icon {:src (:icon o)}]
   [:div.date (:month o) (:year o)]
   [:div.title (:title o)]])

(log (map entry apps))




(defn mylog [& args]
  (let [elem (js/document.getElementById "main") 
        ]
    (aset elem "innerHTML"
          (str (.-innerHTML elem)
               (.replace (prn-str args)
                         (js/RegExp. "<" "g")
                         "&lt;")
               "<br>")
          )))
(defn clearlog []
  (aset (js/document.getElementById "main") "innerHTML" ""))
(clearlog)
(mylog "hello")
(do
  (let [xhr (js/XMLHttpRequest.)
        userpass (js/location.hash.slice 1) 
        [user pass](split userpass #":")]
    (.open xhr "PROPFIND"
           "https://owncloud.solsort.com/remote.php/webdav/"
           true
           user
           pass)
    (aset xhr "withCredentials" true)
  ;  (.setRequestHeader "Authorization" (str "Basic " (js/btoa (str user ":" pass))))
    (aset xhr "onload" #(mylog (.-responseText xhr)))
    (.send xhr)
    (aset js/window "asd" xhr)
    )
  )
js/window.asd
