FROM debian:jessie

RUN apt-get update
RUN apt-get install -y curl git

WORKDIR /caddy

RUN mkdir /caddy; \
    cd /caddy; \
    curl 'https://caddyserver.com/download/build?os=linux&arch=amd64&features=cors,git' | tar xvz

EXPOSE 443
EXPOSE 80

CMD /caddy/caddy -agree -email letsencrypt@solsort.com
