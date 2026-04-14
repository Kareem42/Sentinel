# Project Sentinel: Technical Specification

## 1. Problem Statement
Developers need a way to monitor their personal APIs' uptime without paying for expensive enterprise tools.

## 2. Technical Stack
- **Backend:** Spring Boot (Java) - Chosen for its multi-threading capabilities (needed for concurrent pings).
- **Database:** PostgreSQL - Chosen for relational integrity between Users and Service Logs.
- **Frontend:** React + TypeScript - Chosen for type-safety across the API boundary.

## 3. Database Schema (V1)
### Table: `services`
- `id`: UUID (Primary Key)
- `name`: VARCHAR(255)
- `url`: TEXT
- `status`: VARCHAR(50) (UP, DOWN, PENDING)
- `last_checked`: TIMESTAMP

## 4. API Design (REST)
All endpoints prefixed with `/api/v1`.
- `GET /services`: Returns a list of monitored URLs.
- `POST /services`: Payload `{ "name": string, "url": string }`.