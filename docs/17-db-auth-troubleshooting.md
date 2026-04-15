# Module 17: Database Authentication Errors

## 1. FATAL: role "X" does not exist
This is a PostgreSQL-specific error. It means the connection reached the database, but the **Username** provided is not in the internal Postgres `pg_authid` table.

## 2. Docker Persistence (The Volume Trap)
Postgres containers only initialize the user/database the **first** time they are created. If you change your `docker-compose.yml` environment variables later, you must delete the existing volume (`docker-compose down -v`) for the changes to take effect.

## 3. Interview Talking Point
"When troubleshooting containerized environments, I understand the importance of volume persistence. For instance, I know that changes to environment variables in a Postgres container often require a volume reset to ensure the internal database roles stay in sync with the application configuration."