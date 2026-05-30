# Easy ERP Shop

A modern, cloud-native Point of Sale (POS) and Enterprise Resource Planning (ERP) platform built with Spring Boot, Vue.js, PostgreSQL, Redis, and Apache Kafka.

⚠️ Project Status: Under Active Development

Easy ERP Shop is currently under active development and is not yet feature-complete.

Some modules, workflows, APIs, integrations, and user interfaces are still being implemented, refined, and tested. Features documented in this repository may change as development progresses.

Easy ERP Shop is designed for retail stores, wholesalers, distributors, and multi-branch businesses that require inventory management, sales processing, purchasing workflows, warehouse operations, customer management, accounting integrations, and advanced reporting from a single platform.

The platform focuses on scalability, maintainability, performance, and extensibility through a modern microservice-ready architecture powered by Spring Boot, PostgreSQL, Redis, Apache Kafka, and Vue.js.

---

## Features

### Multi-Tenant Architecture

* Support multiple businesses from a single deployment
* Tenant isolation and secure data segregation
* Scalable architecture for SaaS deployments

### Product & Inventory Management

* Product management
* Categories, brands, units, and currencies
* Batch and serial number tracking
* Stock adjustments
* Inventory valuation
* Barcode generation and scanning
* Low stock alerts

### Warehouse Management

* Multiple warehouses
* Warehouse-to-warehouse transfers
* Stock movement history
* Inventory reconciliation

### Sales Management

* POS billing system
* Quotations
* Sales orders
* Sales returns
* Invoice generation
* Customer-specific pricing
* Discount management
* Tax management

### Purchase Management

* Suppliers management
* Purchase orders
* Purchase returns
* Goods receiving
* Cost tracking

### Customer & Supplier Management

* Customer profiles
* Supplier profiles
* Customer groups
* Credit limits
* Transaction history

### User & Permission Management

* Role-based access control (RBAC)
* Granular permissions
* User management
* Secure authentication using JWT

### Promotion Engine

* Coupons
* Automatic promotions
* Buy X Get Y offers
* Customer group promotions
* Discount stacking rules

### Payments

* Cash payments
* Card payments
* UPI payments
* Razorpay integration
* Multi-payment support
* Payment tracking

### Reporting & Analytics

* Sales reports
* Purchase reports
* Product reports
* Customer reports
* Supplier reports
* Inventory reports
* Profit & loss analysis
* Dashboard statistics

### Import & Export System

* Excel import
* CSV import
* PDF export
* Large dataset processing using Spring Batch

### Notification System

* Email notifications
* Verification emails
* Order notifications
* Stock alerts

### AI Integration

* OpenAI support
* DeepSeek support
* Claude support
* Ollama support
* AI-powered business insights
* Inventory forecasting
* Product search assistance

### File Storage

* Local storage
* AWS S3
* Azure Blob Storage
* Google Cloud Storage

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 3.4
* Spring Security
* Spring Data JPA
* Spring Validation
* Spring Batch
* Spring Retry
* Spring WebSocket
* Spring WebFlux
* JWT Authentication

## Frontend

* Vue 3
* TypeScript
* Pinia
* Vite
* Bootstrap

## Database

* PostgreSQL 15

## Caching

* Redis 7
* Caffeine Cache

## Messaging

* Apache Kafka

## Storage

* Local File Storage
* AWS S3
* Azure Blob Storage
* Google Cloud Storage

## DevOps

* Docker
* Docker Compose
* GitHub Actions

## Monitoring

* Spring Boot Actuator
* OpenTelemetry

---

# Architecture

```text
Frontend (Vue.js)
        |
        v
Spring Boot API
        |
        +--------------------+
        |                    |
        v                    v
 PostgreSQL             Redis Cache
        |
        v
 Apache Kafka
        |
        +--------------------+
        |                    |
        v                    v
 Email Service      Import/Export Jobs

        |
        v
 Storage Providers
 (Local / S3 / Azure / GCS)

        |
        v
 AI Providers
(OpenAI / DeepSeek / Claude / Ollama)
```

---

# Project Structure

```text
easy-erp-shop/
│
├── frontend/
│   ├── src/
│   ├── public/
│   └── Dockerfile
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── build.gradle.kts
│   └── Dockerfile
│
├── docker-compose.yml
├── docker-compose.example.yml
├── .env
├── .env.example
├── README.md
└── LICENSE
```

---

# Prerequisites

Before running the application ensure you have:

* Java 21+
* Node.js 18+
* PostgreSQL 15+
* Redis 7+
* Apache Kafka
* Docker
* Docker Compose

---

# Environment Configuration

Create a copy of the example file:

```bash
cp .env.example .env
```

Configure the required values:

```env
DB_NAME=easyerpshop
DB_USER=postgres
DB_PASSWORD=your_password

JWT_SECRET=replace_with_secure_secret

OPENAI_API_KEY=
DEEPSEEK_API_KEY=
ANTHROPIC_API_KEY=
```

---

# Running with Docker

## Start Services

```bash
docker compose up -d
```

## Stop Services

```bash
docker compose down
```

## View Logs

```bash
docker compose logs -f
```

---

# Backend Development

Navigate to backend:

```bash
cd backend
```

Run application:

```bash
./gradlew bootRun
```

Build:

```bash
./gradlew clean build
```

Run tests:

```bash
./gradlew test
```

---

# Frontend Development

Navigate to frontend:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Run development server:

```bash
npm run dev
```

Build production assets:

```bash
npm run build
```

---

# API Documentation

Swagger UI:

```text
http://localhost:9090/swagger-ui.html
```

OpenAPI Docs:

```text
http://localhost:9090/v3/api-docs
```

---

# Storage Providers

Easy ERP supports multiple storage providers:

* Local Storage
* AWS S3
* Azure Blob Storage
* Google Cloud Storage

Configure the provider using:

```env
STORAGE_ACTIVE=LOCAL
```

Possible values:

```env
LOCAL
AWS_S3
AZURE
GCS
```

---

# AI Providers

Supported AI providers:

* OpenAI
* DeepSeek
* Claude
* Ollama

Example configuration:

```env
OPENAI_API_KEY=your_key
DEEPSEEK_API_KEY=your_key
ANTHROPIC_API_KEY=your_key
```

---

# Security

Security features include:

* JWT Authentication
* Password Encryption
* Role-Based Access Control
* Permission-Based Authorization
* Email Verification
* Rate Limiting
* Secure Secret Management

---

# Docker Services

The default Docker deployment includes:

* PostgreSQL
* Redis
* Zookeeper
* Kafka
* Backend API
* Frontend Application
* Ollama AI Service

---

# Repository Configuration

The repository includes:

```text
.env.example
docker-compose.example.yml

application.properties.example
application-local.properties.example
application-production.properties.example

application-dev.yml.example
application-prod.yml.example
application-storage.yml.example
```

Sensitive values are intentionally excluded from version control.

---

# License

This project is licensed under the MIT License.

See the LICENSE file for details.

---

# Disclaimer

This project is under active development.

Features, APIs, database structures, and integrations may change between releases.
