# Module 7: The Repository Pattern & JPA

## 1. Why JpaRepository?
- **Abstraction:** We interact with Java objects rather than writing raw SQL.
- **Maintenance:** Changing database vendors (e.g., from Postgres to MySQL) requires minimal code changes.

## 2. Interview Talking Point: UUIDs vs Sequential IDs
"I chose to use **UUIDs** as primary keys for the `MonitoredService` entity. This prevents 'ID enumeration' attacks where a malicious user could guess service IDs by incrementing numbers, and it makes future database merges much simpler."

## 3. Paging and Sorting
`JpaRepository` also provides built-in support for Pagination. In a real-world scenario with thousands of logs, we would use this to ensure the API stays fast by only returning 20-50 results at a time.