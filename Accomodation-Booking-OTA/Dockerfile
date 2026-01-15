# ===== STAGE 1: Build =====
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy source code + build files
COPY . .

# Build jar (skip test cho nhanh)
# Ensure the Maven wrapper is executable
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests

# ===== STAGE 2: Run =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar từ stage build
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
