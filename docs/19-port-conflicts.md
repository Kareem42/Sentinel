# Module 19: Port 5432 Conflicts (Shadow Services)

## 1. The Default Port Trap
PostgreSQL defaults to port 5432. Many developers accidentally have a local installation (Postgres.app, Homebrew, or Windows Installer) running as a background service.

## 2. Port Shadowing
If a local service starts first, Docker will either:
- Fail to start the container.
- Map the port "successfully" but the traffic never actually reaches the container because the OS routes it to the local service first.

## 3. The Workaround
If you cannot find/kill the local service, change the **host** port in `docker-compose.yml`:
- `ports: ["5433:5432"]`
- Then update `application.yml` to use `5433`.

## 4. Interview Talking Point
"I encountered a port conflict where a local Postgres service was shadowing my Docker container. I diagnosed this using `lsof` (or `netstat`), identified the conflicting PID, and resolved it to ensure my application was communicating with the containerized environment."