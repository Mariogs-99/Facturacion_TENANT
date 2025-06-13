# Build Angular app
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build -- --output-path=dist/fact-electr-cnr --base-href=/facturacion/

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/fact-electr-cnr /usr/share/nginx/html/facturacion
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]