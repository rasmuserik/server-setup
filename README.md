# solsort.com website etc. setup

TODI/IN-PROGRESS: more declarative setup, with configuration as a clojure data struture, which generates docker-configuration, webserver config, as well as static website.

This is the docker-setup for solsort.com, with various sites.

- tinkuyforum.solsort.com - NodeBB - Redis
- annevoel.dk - wordpress - mysql
- rasmuserik.dk - wordpress - mysql
- solsort.com - static site, also intended to be in this repository
- piwik.solsort.com - piwik - mysql
- apps.solsort.com - github:rasmuserik/apps 
- mubackend.solsort.com - mubackend - couchdb
- owncloud.solsort.com - owncloud - mysql
- ...

behind an caddy-proxy with certificates through letsencrypt.
