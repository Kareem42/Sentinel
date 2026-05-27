# Sentinel

A service uptime monitoring application. Register your external services by URL and Sentinel will automatically ping them every 60 seconds and report their status (UP / DOWN).

## Tech Stack

**Backend**
- Java 17, Spring Boot 3.4.4
- Spring Security + JWT (JJWT 0.11.5) — stateless auth
- Spring Data JPA + PostgreSQL
- SpringDoc OpenAPI (Swagger UI)
- Lombok

**Frontend**
- React 19, TypeScript, Vite
- React Router v7
- Axios
- react-hot-toast

**Infrastructure**
- Docker Compose (3 services: database, API, frontend)

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
