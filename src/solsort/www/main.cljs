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
     :title (replace (last (split id "/")) #"-" " ")
     :icon (str url "/icon.png")})
  )
(defn web [y m id title icon]
  {:year y
   :month (month-names m)
   :url id
   :icon icon
   :title title})
(defn wordpress [y m host]
  {:year y
   :month (month-names m)
   :icon "https://s.w.org/about/images/logos/wordpress-logo-notext-rgb.png"
   :type :wordpress
   :title host
   :url (str "https://" host)})

(def apps
  [
   (git 2016  0 "solsort/FMTools")
   (git 2016  7 "solsort/www")
   (git 2016  7 "NewCircleMovement/Tinkuy-Member-Check")
   (web 2016  5 "https://openplatform.dbc.dk" "Den Åbne Platform" "/assets/openplatform.png")
   (wordpress 2016  5 "alive.solsort.com")
   (git 2016  4 "solsort/MoBibl")
   (web 2016  4 "https://forum.tinkuy.dk" "Tinkuy Forum" "https://forum.tinkuy.dk/uploads/system/site-logo.jpg")
   (git 2016  1 "rasmuserik/Apps")
   (git 2016  1 "rasmuserik/MuBackend")
   (git 2015 12 "rasmuserik/HTML5book")
;   (git 2015 12 "rasmuserik/App-List")
   (git 2015 11 "rasmuserik/BibApp")
   (wordpress 2015  10 "www.annevoel.dk")
   (git 2015  9 "NewCircleMovement/Lemon")
   (wordpress 2015  6 "rasmuserik.com")
   (git 2014 12 "solsort/Visualisering-af-Relationer")
   (git 2014  4 "rasmuserik/Sketch-Note-Draw")
   (git 2014  3 "rasmuserik/Frie-sange")
   (git 2014  3 "rasmuserik/morse-code")
   (git 2014  3 "rasmuserik/Single-touch-snake")
   (git 2014  2 "rasmuserik/KBH-Parking")
   (git 2014  2 "OneTwo360/360-viewer")
   (git 2013  9 "rasmuserik/Art-Quiz")
   (git 2013  4 "rasmuserik/App-Tsartnoc")
   (git 2012  5 "rasmuserik/BlobShot")
   (git 2011  8 "rasmuserik/DKCities")
   (git 2011  8 "rasmuserik/PlanetCute")
   (git 2011  0 "rasmuserik/Timelog")
   (git 2011  3 "rasmuserik/NoteScore")
   (git 2011  0 "rasmuserik/Combigame")
   (git 2011  3 "rasmuserik/Julia4d")
   (git 2011  3 "rasmuserik/JS1K-Brownian")
   (git 2011  3 "rasmuserik/JS1K-Rain")
   (git 2011  3 "rasmuserik/JS1K-Sierpinsky")
   ])

(defn entry [o]
   [:a {:href (:url o)}
    [:div.entry
   [:img.icon {:src (:icon o)}]
   [:div.date (:month o) " " (:year o)]
   [:div.title (:title o)]]])

(load-style!
 {:a
  {:text-decoration :none}
  :body
  {:font-family "Helvetica, Arial, sans-serif"
   :text-align :center}
  :div.entry
  {:display :inline-block
   :vertical-align :top
   :text-align :center
   :font-size 14
   :width 150
   :height 180
   :margin 0}
  :.icon
  {:width 80
   :height 80
   :border-radius 16}
  :.date
  {:font-size "80%"
   :color "#999"}
  }
 'app-style)
(defn main
  ""
  []
  [:div.main
   [:h1 "solsort.com ApS"]
   [:a {:href "https://github.com/rasmuserik"} "open source"]
   [:h1 "HTML5 web/widgets/apps"]

   [:p
   [:a {:href "https://rasmuserik.com"} "RasmusErik Voel Jensen"]
   [:div "+45 60703081 \u00a0 hi@solsort.com"]
]
   [:hr]
   (into [:div.entries]
         (map entry apps))
   ]
  )
(render [main])

#_(log (map entry apps))




#_(defn mylog [& args]
  (let [elem (js/document.getElementById "main") 
        ]
    (aset elem "innerHTML"
          (str (.-innerHTML elem)
               (.replace (prn-str args)
                         (js/RegExp. "<" "g")
                         "&lt;")
               "<br>")
          )))
#_(defn clearlog []
  (aset (js/document.getElementById "main") "innerHTML" ""))
;(clearlog)
;(mylog "hello 2")
#_(do
  (let [xhr (js/XMLHttpRequest.)
        userpass (js/location.hash.slice 1) 
        [user pass](split userpass #":")]
    (.open xhr "PROPFIND"
           "https://owncloud.solsort.com/remote.php/webdav/"
           true
           user
           pass)
    (aset xhr "withCredentials" true)
    (.setRequestHeader xhr "Authorization" (str "Basic " (js/btoa (str user ":" pass))))
    (aset xhr "onload" #(mylog (.-responseText xhr)))
    (.send xhr)
    (aset js/window "asd" xhr)
    )
  )
;js/window.asd
