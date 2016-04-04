FROM debian:jessie

RUN apt-get update
RUN apt-get install -y curl

WORKDIR /caddy

RUN mkdir /caddy; \
    cd /caddy; \
    curl 'https://caddyserver.com/download/build?os=linux&arch=amd64&features=cors' | tar xvz

ADD Caddyfile Caddyfile

EXPOSE 443
EXPOSE 80

CMD /caddy/caddy -agree -email letsencrypt@solsort.com
