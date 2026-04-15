# Module 13: Swagger and OpenAPI 3

## 1. The Value of Live Documentation
Static documentation (like a Word doc) dies the moment the code changes. Swagger is "Live"—it reads the code annotations and stays perfectly in sync with the actual logic.

## 2. Testing the "Happy Path"
We use Swagger to test if a valid `ServiceRequest` results in a `201 Created` and a correctly saved record in Postgres.

## 3. Testing "Edge Cases"
We can use Swagger to send an empty URL. If our `@Valid` annotations are working, we should see a `400 Bad Request` instead of a server error.

## 4. Interview Talking Point
"I integrated OpenAPI 3/Swagger early in the development lifecycle. This served as a 'living contract' that allowed me to test endpoints in isolation and provided the frontend team with a clear, interactive guide on how to consume the API."