# Module 8: Transactions & The Persistence Context

## 1. The `@Transactional` Annotation
By marking a service method as `@Transactional`, Spring ensures the "All or Nothing" rule. If you were saving to two different tables and the second one failed, the first one would "roll back" so you don't have partial data.

## 2. The Return of `save()`
In JPA, the `.save()` method returns a **managed entity**. This is the version of the object that the database has officially "stamped" with:
- A Unique Identifier (UUID).
- Audit timestamps (`createdAt`, `updatedAt`).

## 3. Interview Talking Point
"I made sure my service methods were `@Transactional` to maintain data integrity. I also ensured that my API responses were built from the persistence-managed entity to guarantee that the client receives the exact state of the data as it exists in the database."