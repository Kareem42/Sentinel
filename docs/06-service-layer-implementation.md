# Module 6: The Service Layer & Mapping

## 1. The Mapping Responsibility
The Service layer is responsible for converting DTOs to Entities and vice-versa.
- **Request -> Entity:** So we can save it to the database.
- **Entity -> Response:** So we can send it back to the client.

## 2. Interview Talking Point: "Fat Services, Thin Controllers"
"I kept my controllers 'thin' by moving all business logic and data transformation into the Service layer. This makes the logic reusable and easier to unit test in isolation from the HTTP layer."

## 3. Transactional Integrity
We use the `@Transactional` annotation on service methods to ensure that if a database operation fails halfway through, the whole process rolls back, keeping our data consistent.