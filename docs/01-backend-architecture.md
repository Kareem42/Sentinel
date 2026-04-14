# Module 1: Backend Architecture (Spring Boot)

## 1. The DTO Pattern
We never return our `@Entity` classes directly to the React frontend.
- **Why?** If we change the database column name, we shouldn't break the frontend.
- **Security:** Entities often contain sensitive fields (like `password` or internal `id`s) that shouldn't be leaked.

## 2. API Documentation (Swagger/OpenAPI)
We use `springdoc-openapi-starter-webmvc-ui`. This automatically generates interactive docs.
- **Access URL:** `http://localhost:8080/swagger-ui.html`

## 3. Interview Talking Points
- **"Why DTOs?"** - "I used DTOs to maintain a stable API contract. This allowed me to refactor the database schema without forcing changes on the frontend team."
- **"Soft Deletes vs. Hard Deletes"** - "For the Service logs, I implemented a strategy to keep data for 30 days before archiving, ensuring the database remains performant."