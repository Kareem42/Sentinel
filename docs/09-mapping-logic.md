# Module 9: Mapping Logic & The Service Layer

## 1. Manual Mapping vs. MapStruct
In this stage, we are doing **Manual Mapping**.
- **Pros:** Full control, no "magic" libraries, easy to debug for smaller projects.
- **Cons:** Can become boilerplate-heavy as the project grows.

## 2. The Logic Flow
1. **Receive:** The `ServiceRequest` DTO arrives.
2. **Transform:** We map those fields to a new `MonitoredService` Entity.
3. **Persist:** We save it via the Repository.
4. **Respond:** We map the saved Entity (with its new ID) to a `ServiceResponse`.

## 3. Interview Talking Point
"I handle data transformation in the Service layer to ensure the API's external contract is decoupled from the internal database schema. This allows us to evolve the database without breaking the frontend."