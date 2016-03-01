FROM node:0.12

RUN git clone -b v1.x.x https://github.com/NodeBB/NodeBB /nodebb
RUN cd /nodebb; npm install

VOLUME /nodebb
WORKDIR /nodebb

ADD config.json config.json

EXPOSE 4567
EXPOSE 443
EXPOSE 80

CMD [ "node", "app.js" ]
