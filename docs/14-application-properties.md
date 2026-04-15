# Module 14: Externalized Configuration & DataSources

## 1. What is a DataSource?
A DataSource is a factory for connections to the physical database. Spring Boot uses **HikariCP** by default, which is a high-performance connection pool.

## 2. DDL-Auto Modes
- `none`: No changes made to the schema (Production standard).
- `update`: Hibernate changes the database to match your @Entity classes (Development standard).
- `create-drop`: Creates the schema when starting and deletes it when stopping (Testing standard).

## 3. Interview Talking Point
"I utilized Spring Boot's externalized configuration to manage database credentials. By setting `hibernate.ddl-auto` to `update` during development, I ensured the database schema stayed in sync with my Java entities, accelerating the feedback loop."