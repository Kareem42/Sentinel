# Module 2: Professional API Design

## 1. Controller vs. Service
- **Controller:** The "Traffic Cop." It validates the input and decides which service to call. It should never contain business logic.
- **Service:** The "Brain." It handles the logic (e.g., checking if a service URL is reachable) and interacts with the Repository.

## 2. Status Codes
- `201 Created`: Used for successful POST requests. Includes a `Location` header to the new resource.
- `400 Bad Request`: Used when validation fails (e.g., malformed URL).
- `404 Not Found`: Used when a specific ID doesn't exist.

## 3. The "Service" Contract
By using a Service Interface, we make the code "Testable." We can easily mock the service when writing Unit Tests for the Controller.