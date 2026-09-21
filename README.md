# 🎬 Movie Ticket Reservation

A production-oriented **Movie Ticket Reservation Backend** built with **Spring Boot**, focusing on authentication, ticket reservation, concurrency control, distributed systems, real-time updates, asynchronous processing, payment integration, and cloud services.

The system is designed to handle real-world backend challenges such as **race conditions during seat reservation, distributed locking, asynchronous jobs, real-time client updates, payment confirmation, OTP authentication, caching, and horizontal scaling**.

---

## 🏗️ Architecture Overview

```text
                                      ┌─────────────────────┐
                                      │       Client        │
                                      │  Web / Mobile App   │
                                      └──────────┬──────────┘
                                                 │
                                  ┌──────────────┴──────────────┐
                                  │                             │
                               REST API                       SSE
                                  │                       Real-time Updates
                                  ▼                             │
                     ┌────────────────────────┐                │
                     │      Spring Boot       │◄───────────────┘
                     │      Application       │
                     └───────────┬────────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
              ▼                  ▼                  ▼
       ┌────────────┐     ┌────────────┐     ┌────────────┐
       │ PostgreSQL │     │   Redis    │     │  RabbitMQ  │
       │            │     │            │     │    AMQP    │
       │ Persistent │     │ Cache      │     │ Async Jobs │
       │   Data     │     │ Lock       │     │            │
       │            │     │ Pub/Sub    │     │            │
       └────────────┘     └────────────┘     └────────────┘
                              │
                              │ Pub/Sub
                              ▼
                         SSE Clients

                     External Integrations
                              │
             ┌────────────────┼────────────────┐
             │                │                │
             ▼                ▼                ▼
       ┌──────────┐      ┌──────────┐     ┌──────────┐
       │  Stripe  │      │  Resend  │     │Cloudflare│
       │ Payments │      │  Email   │     │    R2    │
       └──────────┘      └──────────┘     └──────────┘
```

---

# 🛠️ Tech Stack

| Technology                      | Purpose                                               |
| ------------------------------- | ----------------------------------------------------- |
| **Java**                        | Programming language                                  |
| **Spring Boot**                 | Backend application framework                         |
| **Spring Security**             | Authentication and authorization                      |
| **Spring Data JPA / Hibernate** | ORM and database access                               |
| **PostgreSQL**                  | Primary relational database                           |
| **Redis**                       | Caching, distributed locking, temporary data, Pub/Sub |
| **RabbitMQ**                    | Message broker and asynchronous processing            |
| **AMQP**                        | Messaging protocol                                    |
| **Stripe**                      | Payment processing                                    |
| **Cloudflare R2**               | Object/file storage                                   |
| **Resend**                      | Transactional email delivery                          |
| **JWT**                         | Access and refresh token authentication               |
| **Google OAuth**                | Google Sign-In                                        |
| **Server-Sent Events (SSE)**    | Real-time server-to-client updates                    |
| **Docker**                      | Containerization                                      |
| **SpringDoc / OpenAPI**         | API documentation                                     |
| **Spring Boot Actuator**        | Application monitoring                                |
| **Prometheus**                  | Metrics collection                                    |

---

# ✨ Features

## 🔐 Authentication & Authorization

The application provides JWT-based authentication and authorization using **access tokens and refresh tokens**.

### Supported authentication methods

* JWT authentication
* JWT authorization
* Access token
* Refresh token
* Google Sign-In
* OTP-based authentication
* User registration
* User login
* Request OTP
* Verify OTP

### Token Strategy

```text
Access Token
    │
    └── Short-lived
         └── 15 minutes

Refresh Token
    │
    └── Long-lived
         └── 30 days
```

The short-lived access token reduces the exposure window of an access credential while the refresh token allows users to maintain their authenticated session.

---

# 📱 OTP Authentication

The application implements OTP-based authentication using Redis for temporary OTP state.

### OTP Flow

```text
                Request OTP
                    │
                    ▼
             Generate OTP
                    │
                    ▼
              Store in Redis
                    │
                    ▼
              Send via Resend
                    │
                    ▼
              User receives OTP
                    │
                    ▼
              Verify OTP
                    │
             ┌──────┴──────┐
             │             │
          Invalid         Valid
             │             │
             ▼             ▼
           Reject      Register/Login
                           │
                           ▼
                      Generate JWT
                           │
                           ▼
                         Client
```

Redis is suitable for OTP data because OTPs are temporary and require expiration.

---

# 🔑 Google Sign-In

Users can authenticate through **Google OAuth**.

```text
Client
   │
   ▼
Google Authentication
   │
   ▼
Google Authorization
   │
   ▼
Spring Boot
   │
   ▼
Validate Google Identity
   │
   ▼
Find / Create User
   │
   ▼
Generate JWT
   │
   ▼
Client
```

---

# 🎟️ Movie Ticket Reservation

The core functionality is movie ticket reservation.

A typical reservation flow is:

```text
Select Movie
     │
     ▼
Select Theatre
     │
     ▼
Select Show
     │
     ▼
Select Seats
     │
     ▼
Check Availability
     │
     ▼
Acquire Distributed Lock
     │
     ▼
Reserve Seats
     │
     ▼
Create Payment
     │
     ▼
Stripe Checkout
     │
     ▼
Payment Completed
     │
     ▼
Stripe Webhook
     │
     ▼
Confirm Reservation
```

---

# 🔒 Race Condition Handling

One of the most important problems in a movie reservation system is **concurrent seat booking**.

Consider two users trying to reserve the same seat:

```text
User A ────────────────┐
                       │
                       ▼
                     Seat A1
                       ▲
                       │
User B ────────────────┘
```

Without concurrency control:

```text
User A → Check Seat → Available
User B → Check Seat → Available

User A → Reserve Seat
User B → Reserve Seat

❌ Double Booking
```

The application uses **Redis distributed locking** around critical reservation operations.

### Distributed Lock Flow

```text
Request A
    │
    ▼
Acquire Redis Lock
    │
    ▼
Check Seat Availability
    │
    ▼
Reserve Seat
    │
    ▼
Release Lock


Request B
    │
    ▼
Attempt Redis Lock
    │
    ▼
Wait / Retry
    │
    ▼
Acquire Lock
    │
    ▼
Check Seat Availability
    │
    ▼
Seat Already Reserved
    │
    ▼
Reject Request
```

This prevents multiple concurrent requests from entering the critical reservation section at the same time.

The goal is to reduce database contention and protect the system from inconsistent seat reservation state.

---

# ⚡ Redis

Redis is used as more than a traditional cache.

### Redis responsibilities

* Application caching
* OTP temporary storage
* Distributed locking
* Temporary reservation data
* Pub/Sub
* Real-time event propagation
* Reducing database load

Spring Cache is configured to use Redis:

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 30m
```

---

# 📡 Real-Time Updates with Server-Sent Events

The application uses **Server-Sent Events (SSE)** to provide real-time, server-to-client updates.

Instead of continuously polling the server:

```text
Client → GET /seats
Client → GET /seats
Client → GET /seats
Client → GET /seats
...
```

the client maintains a persistent connection:

```text
Client
   │
   │  SSE Connection
   │
   ▼
Spring Boot
   │
   ├──────► Seat Updated
   │
   ├──────► Reservation Updated
   │
   ├──────► Payment Updated
   │
   └──────► Application Event
```

### Distributed SSE

Because the application is designed with distributed deployment in mind, Redis Pub/Sub can be used to propagate events between Spring Boot instances.

```text
                         Redis Pub/Sub
                              │
                 ┌────────────┼────────────┐
                 │            │            │
                 ▼            ▼            ▼
             Instance 1   Instance 2   Instance 3
                 │            │            │
                SSE          SSE          SSE
                 │            │            │
                 ▼            ▼            ▼
              Client A     Client B     Client C
```

For example, an event generated by **Instance 1** can be published through Redis and consumed by other application instances.

This allows clients connected to different application instances to receive relevant real-time updates.

### SSE Use Cases

* Seat availability updates
* Reservation status
* Payment status
* Booking updates
* Application notifications
* Other real-time domain events

---

# 📨 RabbitMQ & AMQP

RabbitMQ is used as the application's **message broker** for asynchronous processing.

Instead of performing every operation synchronously during an HTTP request:

```text
HTTP Request
     │
     ▼
Spring Boot
     │
     ├──── Immediate Operations
     │
     └──── Publish Message
                │
                ▼
            RabbitMQ
                │
                ▼
             Consumer
                │
                ▼
          Background Job
```

This allows work that does not need to block the HTTP request to be processed asynchronously.

### Benefits

* Decoupled components
* Asynchronous processing
* Background jobs
* Reduced request latency for suitable operations
* Better scalability
* Reliable message-based communication

---

# 💳 Stripe Payment

Stripe is used for payment processing.

### Payment Flow

```text
Client
   │
   ▼
Create Reservation
   │
   ▼
Create Stripe Checkout Session
   │
   ▼
Stripe Hosted Checkout
   │
   ▼
Customer Payment
   │
   ▼
Stripe Webhook
   │
   ▼
Spring Boot
   │
   ▼
Validate Webhook
   │
   ▼
Update Payment
   │
   ▼
Confirm Reservation
```

The server uses **Stripe webhook events** to process payment confirmation.

This prevents the backend from relying solely on information returned to the client after checkout.

---

# 📧 Email with Resend

**Resend** is used as the transactional email provider.

Possible email operations include:

* OTP delivery
* Reservation confirmation
* Payment confirmation
* Authentication emails
* Other transactional notifications

```text
Spring Boot
     │
     ▼
   Resend
     │
     ▼
User Email
```

---

# ☁️ Cloudflare R2

**Cloudflare R2** provides object storage for uploaded files.

The application uses R2's S3-compatible API for file storage.

Possible use cases include:

* Movie posters
* Movie images
* Theatre images
* User-uploaded assets
* Other application files

```text
Client
   │
   ▼
Spring Boot
   │
   ▼
Cloudflare R2
   │
   ▼
Object Storage
```

---

# 🗄️ PostgreSQL

PostgreSQL is the primary persistent database.

The application uses:

* Spring Data JPA
* Hibernate
* PostgreSQL

for persistence and database schema management.


---

# 🐳 Docker

The application is containerized using Docker.

The application and its supporting infrastructure can be deployed as containers, allowing consistent environments between development, testing, and production.

Example architecture:

```text
                     Docker Environment
┌──────────────────────────────────────────────────────────┐
│                                                          │
│  ┌─────────────────┐                                     │
│  │ Spring Boot API │                                     │
│  └────────┬────────┘                                     │
│           │                                              │
│     ┌─────┼───────────────┐                              │
│     │     │               │                              │
│     ▼     ▼               ▼                              │
│ PostgreSQL Redis       RabbitMQ                          │
│                                                          │
└──────────────────────────────────────────────────────────┘

                    External Services
                           │
             ┌─────────────┼─────────────┐
             ▼             ▼             ▼
          Stripe         Resend       Cloudflare R2
```

---

# 🏗️ Project Structure

The project is organized around **features, integrations, security, and shared utilities**.

```text
src/
└── main/
    ├── java/
    │   └── ...
    │       ├── feat/
    │       │   └── features/
    │       │
    │       ├── integration/
    │       │   ├── stripe/
    │       │   ├── redis/
    │       │   └── resend/
    │       │
    │       ├── security/
    │       │   ├── config/
    │       │   └── jwt/
    │       │
    │       └── utils/
    │           ├── enums/
    │           ├── entities/
    │           ├── event/
    │           └── eventlisteners/
    │
    └── resources/
        ├── db/
        │   └── migration/
        │
        └── application.yml
```

---

## `feat/`

Contains business features and domain-specific application logic.

```text
feat/
└── features/
    ├── auth/
    ├── user/
    ├── movie/
    ├── theatre/
    ├── show/
    ├── seat/
    ├── reservation/
    └── payment/
```

This keeps business functionality organized around application features.

---

## `integration/`

Contains integrations with external infrastructure and third-party services.

```text
integration/
├── stripe/
├── redis/
└── resend/
```

The purpose is to isolate infrastructure-specific implementation from the main business logic.

---

## `security/`

Contains security-related configuration and authentication mechanisms.

```text
security/
├── config/
└── jwt/
```

Responsibilities include:

* Spring Security configuration
* Authentication filters
* JWT processing
* Authorization
* Security-related configuration

---

## `utils/`

Contains shared application components.

```text
utils/
├── enums/
├── entities/
├── event/
└── eventlisteners/
```

Examples include:

* Shared enums
* Common entities
* Application events
* Event listeners

---

# 📚 API Documentation

The project uses **SpringDoc OpenAPI** for API documentation.

Swagger UI is available at:

```text
/api/swagger-ui.html
```

The application uses:

```yaml
server:
  servlet:
    context-path: /api
```

Therefore API routes are exposed under the `/api` context path.

---

# 📊 Monitoring & Observability

Spring Boot Actuator is enabled for application monitoring.

Configured endpoints include:

```text
health
env
beans
metrics
mappings
prometheus
```

Prometheus-compatible metrics are exposed for monitoring and observability.

Example:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,env,flyway,beans,metrics,mappings,prometheus"
```

---

# ⚙️ Configuration

The application uses environment-based configuration for external services and sensitive credentials.

Example configuration:

```yaml
spring:
  application:
    name: movie-ticket-reservation

  datasource:
    url:

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        flushMode: COMMIT

  flyway:
    baseline-on-migrate: true
    enabled: true
    locations: classpath:db/migration
    validate-on-migrate: true
    baseline-description: "init"
    baseline-version: 0

  data:
    redis:
      url:

  cache:
    type: redis
    redis:
      time-to-live: 30m

  rabbitmq:
    addresses:

  jackson:
    mapper:
      allow-coercion-of-scalars: false

server:
  servlet:
    context-path: /api

springdoc:
  swagger-ui:
    path: /swagger-ui.html

management:
  endpoints:
    web:
      exposure:
        include: "health,env,flyway,beans,metrics,mappings,prometheus"

access:
  token:
    exp: 900000

refresh:
  token:
    exp: 2592000000

cloudflare:
  r2:
    account_id:
    access-key:
    secret-key:
    endpoint:
    bucket-name:
    custom-domain:
    token:

google:
  client:
    id:
    secret:

resend:
  api:
    key:

stripe:
  secret:
    key:
  webhook:
    secret:
```

---

# 🔐 Environment Variables

Sensitive credentials should never be committed to Git.

The following configuration should be provided through environment variables or a secure secret-management system:

```text
DATABASE_URL

REDIS_URL

RABBITMQ_ADDRESSES

R2_ACCOUNT_ID
R2_ACCESS_KEY
R2_SECRET_KEY
R2_ENDPOINT
R2_BUCKET_NAME
R2_CUSTOM_DOMAIN

GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET

RESEND_API_KEY

STRIPE_SECRET_KEY
STRIPE_WEBHOOK_SECRET

JWT_SECRET
```

---

# 🔄 Distributed Architecture

The application is designed with **horizontal scaling** in mind.

Instead of relying on a single Spring Boot process:

```text
                  Client
                    │
                    ▼
             Spring Boot
              Instance 1
```

multiple instances can run behind a load balancer:

```text
                       ┌───────────────┐
                       │    Client     │
                       └───────┬───────┘
                               │
                               ▼
                       ┌───────────────┐
                       │ Load Balancer │
                       └───────┬───────┘
                               │
               ┌───────────────┼───────────────┐
               │               │               │
               ▼               ▼               ▼
          Instance 1      Instance 2      Instance 3
               │               │               │
               └───────────────┼───────────────┘
                               │
                  ┌────────────┼────────────┐
                  ▼            ▼            ▼
             PostgreSQL      Redis      RabbitMQ
```

Redis provides shared state where required, including:

* Distributed locks
* Cache
* OTP data
* Pub/Sub events

RabbitMQ provides shared asynchronous messaging.

This allows application instances to remain relatively stateless while relying on shared infrastructure for distributed coordination.

---

# 🧠 Backend Engineering Concepts

This project demonstrates practical backend engineering concepts including:

### Authentication

* JWT
* Access / refresh tokens
* OTP authentication
* Google OAuth
* Spring Security
* Authorization

### Database

* PostgreSQL
* JPA
* Hibernate
* Database migrations
* Transaction management

### Caching & Concurrency

* Redis caching
* Distributed locking
* Race-condition handling
* Concurrent reservation handling
* Database contention reduction

### Distributed Systems

* Horizontal scaling
* Shared distributed state
* Redis Pub/Sub
* Distributed locking
* Asynchronous communication
* Stateless application design

### Real-Time Communication

* Server-Sent Events
* Persistent HTTP connections
* Redis Pub/Sub
* Distributed event propagation

### Messaging

* RabbitMQ
* AMQP
* Message producers
* Message consumers
* Background processing

### External Services

* Stripe
* Cloudflare R2
* Resend
* Google OAuth

### DevOps

* Docker
* Containerized deployment
* Environment-based configuration
* Application health monitoring
* Prometheus metrics

---

# 🚀 Getting Started

## 1. Clone the repository

```bash
git clone <your-repository-url>

cd movie-ticket-reservation
```

---

## 2. Configure Environment Variables

Configure the required values for:

```text
PostgreSQL
Redis
RabbitMQ
Cloudflare R2
Google OAuth
Resend
Stripe
JWT
```

Do not commit production credentials to the repository.

---

## 3. Start Required Infrastructure

The application requires:

```text
PostgreSQL
Redis
RabbitMQ
```

Make sure these services are running before starting the application.

---

## 4. Run the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or build the application:

```bash
./mvnw clean package
```

---

## 5. Run with Docker

Build the image:

```bash
docker build -t movie-ticket-reservation .
```

Run the container while providing the required environment variables.

---

# 🔑 Token Expiration

Current token expiration configuration:

```yaml
access:
  token:
    exp: 900000

refresh:
  token:
    exp: 2592000000
```

Equivalent durations:

| Token         |   Duration |
| ------------- | ---------: |
| Access Token  | 15 minutes |
| Refresh Token |    30 days |

---

# 🔗 Main System Integrations

```text
                    Spring Boot
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
    PostgreSQL         Redis          RabbitMQ
    Database           Cache          Messaging
                        │
                 ┌──────┴──────┐
                 │             │
              Lock          Pub/Sub
                 │             │
                 │             ▼
                 │            SSE
                 │             │
                 │             ▼
                 │          Clients
                 │
                 ▼
            Reservation
             Protection


External Services
        │
        ├──────────► Stripe
        │             Payment
        │
        ├──────────► Resend
        │             Email
        │
        └──────────► Cloudflare R2
                      File Storage
```

---

# 🎯 Project Goals

The project was built to explore and demonstrate how a modern backend can solve real-world engineering problems rather than simply implementing CRUD APIs.

The main focus areas are:

```text
Authentication
      +
Authorization
      +
Concurrency
      +
Distributed Locking
      +
Caching
      +
Asynchronous Processing
      +
Real-Time Communication
      +
Payment Processing
      +
Cloud Storage
      +
Email Delivery
      +
Containerization
      +
Observability
      +
Horizontal Scaling
```

---

# 📌 Key Takeaways

This project demonstrates a backend architecture combining:

**Spring Boot + PostgreSQL + Redis + RabbitMQ + SSE + Stripe + Cloudflare R2 + Resend + Docker**

with a focus on:

* Secure authentication
* Production-oriented OTP flows
* Concurrent seat reservation
* Race-condition prevention
* Redis distributed locking
* Real-time updates using SSE
* Redis Pub/Sub for distributed events
* Asynchronous processing with RabbitMQ
* Stripe payment and webhook integration
* Cloud object storage
* Transactional email
* Database migration management
* Monitoring and metrics
* Horizontal-scaling considerations

---

## 📄 License

This project is for educational and portfolio purposes.
