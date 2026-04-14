# Module 3: Controller Responsibilities

## 1. Request vs. Response Objects
- **Input (Request DTO):** Contains only what the user provides (e.g., `name`, `url`).
- **Output (Response DTO):** Contains what the system generated (e.g., `id`, `status`, `lastChecked`).

## 2. Dependency Injection
We use **Constructor Injection** to bring in our Service layer. This makes the controller "thin" and the service "fat" (where the logic lives).

## 3. Correct Annotations
- `@RestController`: Marks the class as a web controller.
- `@RequestMapping("/api/v1/services")`: Sets the base path.
- `@PostMapping`: Handles the specific creation logic.