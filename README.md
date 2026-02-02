# KBookin - Accommodation Booking OTA

Full-stack accommodation booking platform built with Spring Boot and React.

## 🛠 Tech Stack

- **Backend:** Java 21, Spring Boot 3
- **Frontend:** React 19, TypeScript, Vite, TailwindCSS
- **Database:** PostgreSQL 14
- **Caching:** Redis 7
- **Messaging:** Kafka, Zookeeper
- **DevOps:** Docker, Docker Compose, Nginx, Cloudflare Tunnel
- **Payment:** VNPay Integration

## 📋 Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.
- [Java 21 JDK](https://adoptium.net/) (for local backend dev).
- [Node.js 20+](https://nodejs.org/) (for local frontend dev).

---

## ⚙️ Environment Setup

### Backend (.env)
Create a `.env` file in the `Accomodation-Booking-OTA` directory. You can copy the structure below:

```properties
# Database
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT Security
JWT_BASE64_SECRET=your_base64_secret_key_here_must_be_long_enough
JWT_ACCESS_TOKEN_VALIDITY_IN_SECONDS=86400
JWT_REFRESH_TOKEN_VALIDITY_IN_SECONDS=604800

# Google OAuth (Optional for local dev if not using login)
GOOGLE_CLIENT_ID=your_google_client_id

# Mail Server (Mailpit)
SPRING_MAIL_USERNAME=null
SPRING_MAIL_PASSWORD=null

# Payment (VNPay)
VNPAY_TMN_CODE=your_vnpay_tmn_code
VNPAY_HASH_SECRET=your_vnpay_hash_secret
```

> **Note:** For Docker Compose, the backend container uses the `.env` file at `Accomodation-Booking-OTA/.env`.

---

## 🚀 Development Setup (Local)

Run the infrastructure in Docker, but run the applications locally for faster debugging.

### 1. Start Infrastructure
Start PostgreSQL, Redis, Kafka, Zookeeper, and Mailpit using Docker Compose:

```bash
docker-compose up -d postgres redis zookeeper kafka mailpit
```

### 2. Start Backend (Spring Boot)
Open a terminal in `Accomodation-Booking-OTA`:

```bash
cd Accomodation-Booking-OTA
./mvnw spring-boot:run
```
*The backend looks for services on localhost ports exposed by Docker.*
- **API:** http://localhost:8080
- **Swagger/OpenAPI:** http://localhost:8080/swagger-ui.html (if configured)
- **Mailpit Web UI:** http://localhost:8025

### 3. Start Frontend (React)
Open a new terminal in `booking-ui`:

```bash
cd booking-ui
npm install
npm run dev
```
- **App:** http://localhost:5173
- The frontend is configured to call the backend at `http://localhost:8080`.

---

## 🚢 Production Setup (Docker)

Run the entire stack (Frontend + Backend + Infrastructure + Nginx) in containers.

### 1. Build and Run
From the project root:

```bash
docker-compose up -d --build
```

### 2. Access the Application
- **Main App:** http://localhost:8080
  - Nginx serves the React app on port 8080.
  - Nginx proxies `/api/*` requests to the Spring Boot backend.
- **Mailpit:** http://localhost:8025

### Service Architecture
- **Nginx (booking-ui):** Proxies requests and serves static files.
- **Spring App:** Backend API (internal access).
- **Cloudflared:** Exposes the application to the internet via Cloudflare Tunnel (if configured).

---

## 🗄 Database & Tools

- **PostgreSQL**: 
  - Host: `localhost` (dev) / `postgres` (docker)
  - Port: `5432`
  - User/Pass: `postgres`/`postgres`
  - Database: `booking_ota`

- **Redis**:
  - Host: `localhost` (dev) / `redis` (docker)
  - Port: `6379`

- **Mailpit**:
  - SMTP: Port `1025`
  - Web UI: Port `8025`
