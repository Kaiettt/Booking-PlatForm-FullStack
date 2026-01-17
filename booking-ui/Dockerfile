# ... (Phần STAGE 1 giữ nguyên) ...

# ===== STAGE 2: Nginx =====
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy file config mới tạo vào đây
COPY default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]