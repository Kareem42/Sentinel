# Scaling Changes

This document describes the changes made to prepare Sentinel for feature growth and a larger number of monitored services, along with the reasoning behind each decision.

---

## 1. Multi-tenancy — services are now scoped to their owner

**What changed**

`MonitoredServiceEntity` gained an `owner` field (`@ManyToOne` to `User`). All service queries in `MonitoredServiceService` now filter by the currently authenticated user. `MonitoredServiceRepository` exposes `findByOwner(User, Pageable)` and `findByIdAndOwner(UUID, User)`.

`DataInitializer` was updated to set the demo user as owner of the three seeded services.

**Why**

The original schema had no owner concept — all services were effectively global and visible to every user. This is a correctness issue that would have required a breaking migration later. Fixing it now, before the data set grows, keeps the change cheap. The `findByIdAndOwner` query also provides free ownership enforcement on DELETE so one user cannot remove another's services.

---

## 2. Parallel health checks with a dedicated thread pool

**What changed**

`MonitoringService` no longer runs checks sequentially in a single `@Transactional` method. Instead:

- A `@Scheduled` tick fires every 30 seconds.
- Each due service is dispatched as a `CompletableFuture` on a `ThreadPoolTaskExecutor` named `monitoringExecutor` (10 core / 50 max threads, 200-slot queue).
- The actual HTTP work and database write happen in `MonitoringCheckExecutor.performCheck(UUID)`, a separate Spring bean annotated `@Transactional`, so each check owns its own short-lived transaction.

**Why**

With sequential checks, a single service that hangs for 10 seconds delays every subsequent check. As the number of monitored services grows, the 60-second window would quickly become insufficient. Parallel dispatch means 200 services can all be checked concurrently; the only bottleneck becomes the thread pool size, which is tunable via `AsyncConfig`.

Separating `MonitoringCheckExecutor` from `MonitoringService` is required because `@Transactional` only works through Spring's proxy — a method calling itself on the same bean bypasses the proxy and gets no transaction. Moving the check into a separate bean gives each parallel task a proper, independent transaction.

---

## 3. Per-service configurable check intervals

**What changed**

`MonitoredServiceEntity` gained `checkIntervalSeconds` (default 60, enforced min 30 / max 86400 via validation on `ServiceRequest`). `MonitoringService` now evaluates whether each service is "due" before dispatching — a service with a 300-second interval will be skipped on ticks that land within 300 seconds of its last check.

**Why**

A hardcoded 60-second interval is a product constraint masquerading as a default. Critical internal APIs may need sub-minute checks; low-priority third-party URLs may only need hourly checks. Configurable intervals let users express this without requiring separate scheduler configurations, and they reduce unnecessary outbound HTTP traffic.

---

## 4. Response time tracking

**What changed**

`MonitoringCheckExecutor` wraps the `HttpClient.send()` call with a millisecond timer. The result is stored as `lastResponseTimeMs` on `MonitoredServiceEntity` (denormalised for fast list queries) and logged to `ServiceCheckLog`. The frontend displays it in the service table.

**Why**

Status alone ("UP / DOWN") has limited diagnostic value. Response time trends are often the first indicator of a degrading service before it goes fully down. Storing it denormalised on the entity means list queries remain a single `SELECT` — no join to the log table required.

---

## 5. Check history — `ServiceCheckLog`

**What changed**

A new `service_check_logs` table stores every ping result: service reference, status, response time, and timestamp. An index on `(service_id, checked_at DESC)` makes recent-history queries fast. `ServiceCheckLogRepository` exposes `findTop5ByServiceOrderByCheckedAtDesc` for future use.

**Why**

Without history there is no uptime percentage, no incident timeline, and no way to distinguish a transient blip from a sustained outage. The table is append-only (rows are never updated), which makes it straightforward to prune, archive, or aggregate later. Because status and response time are also written back to the parent entity, existing list queries are unaffected — the log is purely additive.

---

## 6. DELETE endpoint

**What changed**

`DELETE /api/v1/service/{id}` was added to `MonitoredServiceController`. The service layer verifies ownership before deleting. The frontend `ServiceList` gained a "Remove" button that calls the endpoint and optimistically removes the row from local state.

**Why**

The original API had no way to remove a service. Any accidental registration or decommissioned endpoint would persist forever. This is the most basic CRUD gap and the one users encounter first.

---

## 7. Paginated service list

**What changed**

`GET /api/v1/service` now accepts Spring `Pageable` query parameters (`?page=N&size=N&sort=field,dir`) and returns a `Page<ServiceResponse>` instead of a plain list. The default is page 0, size 20, sorted by `createdAt` descending. The frontend hook was updated to read `response.data.content` and expose `totalElements` and `totalPages`.

**Why**

`findAll()` with no limit returns every row in the table in a single query. For users with hundreds of services this becomes a slow, large payload. Pagination makes the list query O(page size) regardless of total row count, and the metadata (`totalElements`, `totalPages`) gives the frontend what it needs to build navigation controls when that becomes necessary.

---

## Summary of new files

| File | Purpose |
|------|---------|
| `entity/ServiceCheckLog.java` | Append-only check history record |
| `repository/ServiceCheckLogRepository.java` | Queries over check history |
| `config/AsyncConfig.java` | Thread pool for parallel monitoring |
| `service/MonitoringCheckExecutor.java` | Single-service health check with its own transaction |
| `docs/21-scaling-changes.md` | This document |

## Summary of modified files

| File | Change |
|------|--------|
| `entity/MonitoredServiceEntity.java` | Added `owner`, `checkIntervalSeconds`, `lastResponseTimeMs` |
| `repository/MonitoredServiceRepository.java` | Added owner-scoped queries |
| `dto/ServiceRequest.java` | Added optional `checkIntervalSeconds` |
| `dto/ServiceResponse.java` | Added `lastChecked`, `lastResponseTimeMs`, `checkIntervalSeconds` |
| `service/MonitoredServiceService.java` | Multi-tenancy, delete, pagination |
| `service/MonitoringService.java` | Parallel dispatch, per-service interval logic |
| `controller/MonitoredServiceController.java` | DELETE endpoint, `Page` return type |
| `config/DataInitializer.java` | Associate seeded services with demo user |
| `sentinel-ui/src/types.ts` | Updated `MonitoredServiceResponse`, added `PageResponse<T>` |
| `sentinel-ui/src/hooks/useServices.ts` | Page response handling, `deleteService` |
| `sentinel-ui/src/components/ServiceList.tsx` | Delete button, response time, last-checked, interval columns |
| `sentinel-ui/src/components/ServiceForm.tsx` | Optional check interval input |
| `sentinel-ui/src/App.tsx` | Wire new props through to `ServiceList` |
| `README.md` | Updated features list, API table, pagination docs |
