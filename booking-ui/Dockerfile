# ===== STAGE 1: Build React =====
FROM node:20-alpine AS builder
WORKDIR /app

COPY booking-ui/package*.json ./
RUN npm install

COPY booking-ui .
RUN npm run build

# ===== STAGE 2: Nginx =====
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
