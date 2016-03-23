FROM node:0.12

#RUN git clone -b v1.x.x https://github.com/NodeBB/NodeBB /nodebb
RUN git clone -b v1.0.1 https://github.com/NodeBB/NodeBB /nodebb

WORKDIR /nodebb

RUN npm install
RUN npm install nodebb-plugin-emailer-mailgun nodebb-plugin-sso-facebook nodebb-plugin-sso-google

EXPOSE 4567
EXPOSE 443
EXPOSE 80

CMD [ "node", "app.js" ]
