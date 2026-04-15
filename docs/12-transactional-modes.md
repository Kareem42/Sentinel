# Module 12: Transactional Modes (Read-Write vs. Read-Only)

## 1. Default `@Transactional`
- **Use Case:** POST, PUT, DELETE, PATCH.
- **Behavior:** Opens a "Read-Write" transaction. Hibernate will check for changes and "flush" them to the database at the end of the method.

## 2. `@Transactional(readOnly = true)`
- **Use Case:** GET, SEARCH.
- **Behavior:** Tells Hibernate it doesn't need to do "dirty checking" (checking if objects changed). This reduces CPU and memory usage, making your app faster.

## 3. Interview Talking Point
"I use `readOnly = true` on all my fetch operations to optimize performance and signal to other developers that the method should not modify the database state. For creation logic, I use standard transactions to ensure atomicity."