(ns solsort.www.main
  (:require-macros
   [com.rpl.specter.macros :as s :refer [select]]
   [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [com.rpl.specter :as s]
   [solsort.toolbox.misc :refer [<blob-url]]
   [solsort.util
    :refer
    [<p <ajax <seq<! js-seq normalize-css load-style! put!close!
     parse-json-or-nil log page-ready render dom->clj next-tick]]
   [reagent.core :as reagent :refer []]
   [cljs.reader :refer [read-string]]
   [clojure.data :refer [diff]]
   [clojure.string :as string :refer [replace split blank?]]
   [cljs.core.async :as async :refer [>! <! chan put! take! timeout close! pipe]]))

;; util
(defn ->f [start & fs] (reduce #(%2 %1) start fs))

;; write file to github-hack
(defonce github-token
  (if (.startsWith js/location.hash "#solsortgithub=")
    (let [token (aget (.split js/location.hash "=") 1)]
      (js/localStorage.setItem "www-token" token)
      token)
    (js/localStorage.getItem "www-token")))
(defn <myjax [url content]
  (let [method (if content "PUT" "GET")
        c (chan)
        xhr (js/XMLHttpRequest.)]
    (aset xhr "onreadystatechange"
          (fn [a]
            (log 'ready-state (aget xhr "readyState"))
            (when (= 4 (aget xhr "readyState"))
              (put!close! c (JSON.parse (aget xhr "responseText"))))))
    (.open xhr method url)
    (.setRequestHeader xhr "Authorization" (str "token " github-token))
    (.send xhr content)
    c))
(defn <github-write [repos path content]
  (go
    (let [url (str "https://api.github.com/repos/" repos "/contents/" path)
          result (<! (<myjax url nil))
          empty (= "Not Found" (aget result "message"))
          sha (aget result "sha")
          old-content (js/atob (aget result "content"))
          ]
      (log 'a)
      (log 'gh-write 'result result
           (when-not (= content old-content)
            (<! (<myjax
                 url
                 (js/JSON.stringify
                  (clj->js {:message "automated commit"
                            :sha sha
                            :content (js/btoa content)}))))))
      (go)
      content
     )))

;(github-write "rasmuserik/test" "text.txt" "hello-123\n")


;; Definitions of apps
(def month-names
  ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"])
(defn git [y m id]
  (let [short-name (second (string/split id #"/"))
        url (str "https://" short-name ".solsort.com")]
    {:year y
     :month (month-names m)
     :url url
     :id id
     :short-name (.toLowerCase short-name)
     :type :git
     :title (replace (last (split id "/")) #"-" " ")
     :icon (str url "/icon.png")}))
(defn web [y m id title icon]
  {:year y
   :month (month-names m)
   :url id
   :type :web
   :icon icon
   :short-name (-> title
                   (replace (js/RegExp "\\..*" "g") "")
                   (replace (js/RegExp " " "g") "-")
                   (.toLowerCase))
   :title title})
(defn wordpress [y m host]
  {:year y
   :month (month-names m)
   :icon "https://s.w.org/about/images/logos/wordpress-logo-notext-rgb.png"
   :type :wordpress
   :short-name (.toLowerCase (replace (replace host #"^www\." "") (js/RegExp "\\..*" "g") ""))
   :host host
   :url (str "https://" host)})
#_(def app2
  (doall (map #(apply (get {"wordpress" wordpress "web" web "git" git} (first %)) (rest %)) app-list)))
#_(log app2)
(def apps
  [
   (git 2016  0 "rasmuserik/writings")
   (git 2016  0 "solsort/MoBibl")
   (git 2016  9 "solsort/MiniLD-70")
   (git 2016  9 "solsort/FMTools")
   (git 2016  8 "solsort/Dots-and-Boxes")
   (git 2016  8 "solsort/Here")
   (git 2016  8 "solsort/www")
   (git 2016  7 "NewCircleMovement/Tinkuy-Member-Check")
   (web 2016  5 "https://openplatform.dbc.dk" "Den Åbne Platform" "/assets/openplatform.png")
   (web 2016  5 "https://github.com/solsort/bion" "Bion data format" "https://github.com/solsort/bion/raw/master/icon.jpg")
   (wordpress 2016  5 "alive.solsort.com")
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
   (git 2011  3 "rasmuserik/JS1K-Sierpinsky")])

;; Generate configuration files
(def caddy-base "
solsort.com {
  redir https://www.solsort.com{uri}
}
http://piwik.localhost, piwik.solsort.com, piwik.rasmuserik.com {
  proxy / http://piwik {
    proxy_header Host {host}
    proxy_header X-Real-IP {remote}
    proxy_header X-Forwarded-Proto {scheme}
  }
  cors
}
http://owncloud.localhost, owncloud.solsort.com {
  proxy / http://owncloud {
    proxy_header Host {host}
    proxy_header X-Real-IP {remote}
    proxy_header X-Forwarded-Proto {scheme}
  }
  header / Access-Control-Allow-Headers Content-Type
  header / Access-Control-Allow-Credentials true
  cors {
    methods POST,GET,OPTIONS,PUT,DELETE,PROPFIND,PROPPATCH,MKCOL,COPY,MOVE,LOCK,UNLOCK
    allowed_headers Access-Control-Allow-Headers,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization
  }
}
http://mysql.localhost, mysql.solsort.com {
  proxy / http://mysql-admin
}
http://couch.localhost, couch.solsort.com {
  cors
  proxy / http://couchdb:5984
}
http://tinkuy-forum.localhost, tinkuy-forum.solsort.com, forum.tinkuy.dk {
  proxy / http://tinkuy-nodebb:4567 {
    websocket
  }
}
fmproxy.solsort.com {
  proxy / http://app.fmtools.dk {
    proxy_header Host app.fmtools.dk
  }
  header / Access-Control-Allow-Headers Content-Type
  header / Access-Control-Allow-Credentials true
  cors
}")
(defn caddy-file []
  (apply
   str
   "\n# AUTOGENERATED FILE, DO NOT EDIT\n"
   caddy-base
   (map
    (fn [o]
      (case (:type o)
        :web ""
        :git (str "
" (:short-name o) ".solsort.com {
  git https://github.com/" (:id o) ".git {
    hook /git-update
  }
  root /apps/" (:short-name o) "
  cors
}")
        :wordpress (str "
http://" (:short-name o) ".localhost, " (:host o) "  {
  proxy / http://" (:short-name o) " {
    proxy_header Host {host}
    proxy_header X-Real-IP {remote}
    proxy_header X-Forwarded-Proto {scheme}
  }
  cors
}")))
    apps)))
(def docker-base
  {:version "2"
   :services
   {"tinkuy-nodebb"
    {:build "./nodebb"
     :links ["tinkuy-redis"]
     :volumes ["./tinkuy-nodebb.config.json:/nodebb/config.json:ro"
               "tinkuy-nodebb:/nodebb/public/uploads"]}
    "tinkuy-redis"
    {:image "redis:3"
     :volumes ["tinkuy-redis:/data"]}
    "couchdb"
    {:image "couchdb:1"
     :volumes ["couchdb:/usr/local/var/lib/couchdb"]}
    "owncloud-mysql"
    {:image "mysql"
     :environment {"MYSQL_ROOT_PASSWORD" "$PW3"}
     :volumes ["owncloud-mysql:/var/lib/mysql"]}
    "owncloud"
    {:image "owncloud:9"
     :links ["owncloud-mysql"]
     :volumes ["owncloud:/var/www/html"]}
    "piwik-mysql"
    {:image "mysql"
     :environment {"MYSQL_ROOT_PASSWORD" "$PW2"}
     :volumes ["piwik-mysql:/var/lib/mysql"]}
    "piwik"
    {:image "marvambass/piwik"
     :links ["piwik-mysql"]
     :environment
     {"PIWIK_MYSQL_USER" "root"
      "PIWIK_MYSQL_PASSWORD" "$PW2"
      "PIWIK_MYSQL_HOST" "piwik-mysql"
      "PIWIK_ADMIN_PASSWORD" "$PW2"
      "SITE_URL" "https://solsort.com"}}
    "mysql-admin"
    {:image "clue/adminer" ; TODO
     :links ["rasmuserik-mysql"]
     }
    "caddy"
    {:build "./caddy"
     :links ; TODO
     ["tinkuy-nodebb"
      "couchdb"
      "rasmuserik"
      "annevoel"
      "alive"
      "mysql-admin"
      "piwik"
      "owncloud"
      ]
     :volumes
     ["./Caddyfile:/caddy/Caddyfile:ro"
      "caddy:/root/.caddy"
      "../apps:/apps"]
     "ports" ["0.0.0.0:80:80" "0.0.0.0:443:443"]}}
   :volumes
   {"caddy" {"driver" "local"}
    "couchdb" {"driver" "local"}
    "tinkuy-nodebb" {"driver" "local"}
    "tinkuy-redis" {"driver" "local"}
    "piwik-mysql" {"driver" "local"}
    "owncloud" {"driver" "local"}
    "owncloud-mysql" {"driver" "local"}}})
(defn docker-compose []
  (let [wordpresses (filter #(#{:wordpress} (:type %)) apps)]
    (->f docker-base
         #(log %)
         (fn [o]
           (assoc
            o :services
            (into
             (:services o)
             (map
              #(let [short-name (:short-name %)
                     mysql (str short-name "-mysql")]
                 [short-name
                  {:image "wordpress"
                   :links [mysql]
                   :environment
                   {"WORDPRESS_DB_HOST" mysql
                    "WORDPRESS_DB_PASSWORD" "$PW1"}
                   :volumes [(str short-name "-wordpress:/var/www/html")]}])
              wordpresses))))
         (fn [o]
           (assoc
            o :services
            (into
             (:services o)
             (map
              #(vector
                (str (:short-name %) "-mysql")
                {:image "mysql"
                 :environment {"MYSQL_ROOT_PASSWORD" "$PW1"}
                 :volumes [(str (:short-name %) "-mysql:/var/www/html")]})
              wordpresses))))
         #(assoc
           % :volumes
           (into
            (sorted-map)
            (concat
             (:volumes %)
             (map (fn [o] [(str (:short-name o) "-wordpress") {:driver :local}]) wordpresses)
             (map (fn [o] [(str (:short-name o) "-mysql") {:driver :local}]) wordpresses))))
         #(assoc % :services
                 (into (sorted-map) (map (fn [[k v]]
                                           [k (assoc v :restart :always)])
                                         (:services %))))
         #(clj->js %)
         #(js/JSON.stringify % nil 2))))

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
   :color "#999"}}
 'app-style)
(defn entry [o]
  [:a {:href (:url o)}
   [:div.entry
    [:img.icon {:src (:icon o)}]
    [:div.date (:month o) " " (:year o)]
    [:div.title (:title o)]]])
(defn main
  ""
  []
  [:div.main
   [:h1 "solsort.com ApS"]
   [:a {:href "https://github.com/rasmuserik"} "open source"]
   [:h1 "HTML5 web/widgets/apps"]

   [:p
    [:a {:href "https://rasmuserik.com"} "RasmusErik Voel Jensen"]
    [:div "+45 60703081 \u00a0 hi@solsort.com"]]
   [:hr]
   (into [:div.entries]
         (map entry apps))
   (if github-token
       [:div {:style {:text-align :left}}
     [:h1 "Caddyfile"]
     [:pre {:style {:text-align :left}} (caddy-file)]
     [:h1 "docker-compose.yml"]
        [:pre {:style {:text-align :left}} (docker-compose)]
        [:h1 "Upload "
         [:button.ui.button
          {:on-click
           #(go
              (<! (<github-write "solsort/www" "Caddyfile" (caddy-file)))
              (<! (<github-write "solsort/www" "new-docker-compose.yml" (docker-compose)))
             )}
          "to github"]]]
       "")])
(render [main])


#_(do ; github experiments
    (def localhost-client-id "cb3c5fedfff7f29b0d7a")
    (case (js/localStorage.getItem "login-state")
      "github-login" (go
                       (js/localStorage.setItem "login-state" "")
                       (log 'github-login (.replace js/location.search #".*=" ""))
                                        ; Not possible pure browser-side.
                       (log (<! (<ajax
                                 "https://github.com/login/oauth/access_token"
                                 #js {:client_id "cb3c5fedfff7f29b0d7a"
                                      :client_secret "XXXXX"
                                      :code (.replace js/location.search #".*=" "")}))))
      nil)

    (go
      (defn <get [url data]
        (let [c (chan)
              xhr (js/XMLHttpRequest.)]
          (aset xhr "onreadystatechange"
                (fn [a]
                  (when (= 4 (aget xhr "readyState"))
                    (put!close! c (aget xhr "responseText")))))
          (.open xhr "PUT" url)
          (.send xhr "{\"message\": \"my commit message\",\"content\": \"bXkgbmV3IGZpbGUgY29udGVudHM=\"}")
          c))
      #_(log (<! (<get
                  "https://api.github.com/repos/solsort/www/contents/blah.txt"))))
    (defn github-login []
      (js/localStorage.setItem "login-state" "github-login")
      (aset js/location "href"
            (str "https://github.com/login/oauth/authorize"
                 "?client_id=" localhost-client-id
                 "&scope=public_repo"
                                        ; "&state=" (js/Math.random)
                                        ; (github-login)
))))
#_(do ; webdav experiments
    (log (map entry apps))
    (defn mylog [& args]
      (let [elem (js/document.getElementById "main")]
        (aset elem "innerHTML"
              (str (.-innerHTML elem)
                   (.replace (prn-str args)
                             (js/RegExp. "<" "g")
                             "&lt;")
                   "<br>"))))
    (defn clearlog []
      (aset (js/document.getElementById "main") "innerHTML" ""))
;(clearlog)
;(mylog "hello 2")
    (do
      (let [xhr (js/XMLHttpRequest.)
            userpass (js/location.hash.slice 1)
            [user pass] (split userpass #":")]
        (.open xhr "PROPFIND"
               "https://owncloud.solsort.com/remote.php/webdav/"
               true
               user
               pass)
        (aset xhr "withCredentials" true)
        (.setRequestHeader xhr "Authorization" (str "Basic " (js/btoa (str user ":" pass))))
        (aset xhr "onload" #(mylog (.-responseText xhr)))
        (.send xhr)
        (aset js/window "asd" xhr)))
;js/window.asd
)
