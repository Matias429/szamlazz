FROM node:24.13.0-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine 

RUN mkdir -p /tmp/nginx/client_body && \
    chown -R nginx:nginx /tmp/nginx /var/cache/nginx /var/log/nginx /etc/nginx

COPY nginx.conf /etc/nginx/nginx.conf
COPY --chown=nginx:nginx ./certs /etc/nginx/certs
COPY --chown=nginx:nginx --from=builder /app/dist/*/browser /usr/share/nginx/html

USER nginx

EXPOSE 8443

CMD ["nginx", "-g", "daemon off;"]