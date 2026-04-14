# Module 4: Dependency Injection & Testing

## 1. Why Constructor Injection?
- **Immutability:** We can mark our service fields as `final`, ensuring they aren't changed after the controller is initialized.
- **Testability:** We can easily pass a "Mock" service into the constructor during Unit Testing.
- **Clear Dependencies:** You can see exactly what a class needs to function just by looking at the constructor.

## 2. The Flow
1. Client sends `ServiceRequest` (JSON).
2. Controller receives `ServiceRequest`.
3. Controller calls `service.save(request)`.
4. Service converts `Request` -> `Entity`, saves to DB, then converts `Entity` -> `Response`.
5. Controller returns `ServiceResponse` (JSON).