# =========================
# BUILD STAGE
# =========================
FROM node:20-alpine AS builder

WORKDIR /app

COPY booking-ui/package*.json ./
RUN npm install

COPY booking-ui/ .
RUN npm run build

# =========================
# NGINX STAGE
# =========================
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY booking-ui/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]