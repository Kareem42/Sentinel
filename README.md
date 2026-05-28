# Project Sentinel

A lightweight, self-hosted service uptime monitoring application. Register your external microservices or websites by URL, and Sentinel will automatically track their availability, pinging them on a background thread and reporting their real-time status.

## Tech Stack

**Backend**
- Java 17, Spring Boot 3.4.4
- Spring Security + JWT (JJWT 0.11.5) — stateless authentication & role-based access filtering
- Spring Data JPA + PostgreSQL - Relational storage with robust data mapping
- SpringDoc OpenAPI (Swagger UI) - Automated API documentation
- Lombok - Boilerplate reduction

**Frontend**
- React 19, TypeScript, Vite - Modern, type-safe, ultra-fast UI rendering
- React Router v7 - Declarative client-side routing
- Axios - Centralized HTTP client with request/response interceptor for JWT injection
- react-hot-toast - Responsive asynchronous UI notifications

**Infrastructure**
- Docker Compose - Multi-container orchestration managing 3 isolated services (Database, API, and Frontend UI) over a shared virtual network

## Features

- User registration and JWT-based login
- Add services to monitor by URL and name
- Automated health checks every 60 seconds via HTTP HEAD requests
- Per-service status (UP / DOWN) with last-checked timestamp
- Dashboard to view all monitored services and add new ones

## Running with Docker

You only need **Docker Desktop** — no Java, Node.js, or PostgreSQL required locally.

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) and make sure it's running.
2. Clone the repository and open a terminal at the project root.
3. Run:

```bash
docker compose up --build
```

| Service      | URL                          |
|--------------|------------------------------|
| Frontend     | http://localhost:5173        |
| Backend API  | http://localhost:8080        |
| Swagger UI   | http://localhost:8080/swagger-ui/index.html |
| PostgreSQL   | localhost:5432 (`sentinel_db`) |

### Demo credentials

The app seeds a ready-to-use account and three example monitored services on first boot — no manual setup required.

| Field    | Value      |
|----------|------------|
| Username | `demo`     |
| Password | `demo1234` |

Pre-seeded services: **GitHub**, **Google**, and a public **JSON placeholder API**. You can add, remove, or replace them from the dashboard after logging in.

To stop all services:

```bash
docker compose down
```

To stop and remove persisted database data:

```bash
docker compose down -v
```

## API Endpoints

| Method | Path                                | Auth Required | Description              |
|--------|-------------------------------------|---------------|--------------------------|
| POST   | `/api/v1/registration/register`     | No            | Register a new user      |
| POST   | `/api/v1/auth/login`                | No            | Login, returns JWT token |
| POST   | `/api/v1/service`                   | Yes           | Add a service to monitor |
| GET    | `/api/v1/service`                   | Yes           | List all monitored services |

Authenticated requests require an `Authorization: Bearer <token>` header.

## Environment Variables

The backend reads the following environment variables (with fallback defaults for local dev):

| Variable                    | Default                        | Description              |
|-----------------------------|--------------------------------|--------------------------|
| `JWT_SECRET`                | Insecure dev default (change in prod) | HS256 signing key |
| `SPRING_DATASOURCE_URL`     | `jdbc:postgresql://localhost:5432/sentinel_db` | Database URL |
| `SPRING_DATASOURCE_USERNAME`| `user`                         | DB username              |
| `SPRING_DATASOURCE_PASSWORD`| `password`                     | DB password              |

> **Note:** The default `JWT_SECRET` in `application.yaml` is for development only. Set a strong secret via environment variable before deploying.

## Project Structure

```
Sentinel/
├── sentinel-backend/          # Spring Boot application
│   └── main/java/com/backend/sentinel/
│       ├── config/            # Security, CORS, data initialization
│       ├── controller/        # Auth, Registration, MonitoredService endpoints
│       ├── dto/               # Request/response objects
│       ├── entity/            # JPA entities (User, MonitoredServiceEntity)
│       ├── repository/        # Spring Data JPA repositories
│       ├── security/          # JWT filter and service
│       └── service/           # Business logic + scheduled monitoring
├── sentinel-ui/               # React frontend
│   └── src/
│       ├── context/           # AuthContext (JWT token management)
│       ├── pages/             # Login, Register
│       ├── components/        # ServiceForm, ServiceList
│       ├── hooks/             # useServices
│       └── api/               # Axios configuration
├── docker-compose.yml
└── Dockerfile
```
