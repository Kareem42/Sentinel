# Module 1: System Architecture & Design

## 1. The Stack Overview
- **Frontend:** React (TS) - Handles UI/UX and client-side state.
- **Backend:** Spring Boot - Handles business logic, auth, and DB communication.
- **Database:** PostgreSQL - Relational storage for users and metrics.
- **DevOps:** Docker (Containerization) & AWS (Cloud Hosting).

## 2. Key Architectural Decisions
- **Stateless Authentication:** We use JWT (JSON Web Tokens) so the backend can scale horizontally without sharing session state.
- **Type Safety:** Using TypeScript on the front and Java on the back ensures a "contract" between data structures, reducing runtime errors.
- **Containerization:** Using Docker ensures "it works on my machine" translates perfectly to "it works in production."

## 3. Immediate Action Items
1. Install Docker Desktop.
2. Initialize Spring Boot via [start.spring.io](https://start.spring.io) (Deps: Web, JPA, Postgres Driver, Lombok).
3. Initialize React via `npm create vite@latest`.