# ===== STAGE 1: Build React =====
# QUAN TRỌNG: Phải có chữ "AS builder" ở đây
FROM node:20-alpine AS builder 
WORKDIR /app

# Vì context build từ file docker-compose là thư mục gốc (.), 
# nên ta phải trỏ vào thư mục booking-ui
COPY booking-ui/package*.json ./
RUN npm install

COPY booking-ui/ .
RUN npm run build

# ===== STAGE 2: Nginx =====
FROM nginx:alpine

# Copy kết quả từ stage builder sang
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy file config (đảm bảo file này nằm trong booking-ui/default.conf)
COPY booking-ui/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
