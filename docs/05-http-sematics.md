# Module 5: HTTP Semantics & REST

## 1. 200 OK vs 201 Created
- **200 OK:** "I found what you asked for" or "I updated it successfully."
- **201 Created:** "I have successfully created a new resource, and here is its unique ID."

## 2. Why use ResponseEntity?
Using `ResponseEntity<T>` provides full control over the HTTP response.
- `ResponseEntity.status(HttpStatus.CREATED).body(data)`
- It makes the API "Self-Documenting." When a developer sees a `201`, they know a side-effect happened in the database.

## 3. Interview Talking Point
"I chose to return `201 Created` for my POST endpoints to adhere to RESTful standards and provide the client with the newly generated UUID immediately, reducing the need for a follow-up GET request."