# Module 16: Docker Compose & Infrastructure as Code

## 1. What is Docker Compose?
Docker Compose is a tool for defining and running multi-container Docker applications. Instead of running long `docker run` commands, we define our infrastructure in a `.yml` file.

## 2. Port Mapping (`5432:5432`)
- The **left** side is the port on your laptop.
- The **right** side is the port inside the Docker container.
- This allows our Spring Boot app (running on your laptop) to talk to the DB (running in Docker) via `localhost:5432`.

## 3. Interview Talking Point
"I used Docker Compose to manage my local development environment. This ensured that the database configuration was version-controlled and that any developer could spin up the entire stack with a single command, eliminating 'environment drift'."