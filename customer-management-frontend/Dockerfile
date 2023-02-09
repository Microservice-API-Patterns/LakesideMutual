FROM node:16 as build
WORKDIR /usr/src/app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:stable-alpine
COPY nginx.vh.default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/src/app/build /usr/share/nginx/html
EXPOSE 80
WORKDIR /usr/share/nginx/html

RUN apk add --no-cache nodejs npm
RUN npm install -g @beam-australia/react-env@3.1.1
ADD .env ./
ADD entrypoint.sh /var/entrypoint.sh
ENTRYPOINT ["/var/entrypoint.sh"]

CMD ["nginx", "-g", "daemon off;"]
