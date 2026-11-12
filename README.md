# Distributed Job Processing & Workflow Orchestration Platform

A runnable Spring Boot reference implementation of an asynchronous job platform using PostgreSQL, Redis, RabbitMQ, Docker Compose, Prometheus and Grafana.

## Requirements
- Docker Desktop / Docker Engine + Compose
- Java 21 and Maven (only if running outside Docker)

## Run
```bash
cp .env.example .env
# Replace JWT_SECRET with a random secret of at least 32 characters.
docker compose up --build
```
API: http://localhost:8080  
PostgreSQL: localhost:5433 (override with `POSTGRES_PORT` if needed)  
RabbitMQ management: http://localhost:15672 (jobs / jobs)  
Prometheus: http://localhost:9090  
Grafana: http://localhost:3000 (admin / admin)

## API
Register:
```bash
curl -X POST http://localhost:8080/api/auth/register -H 'Content-Type: application/json' -d '{"email":"dev@example.com","password":"Password123"}'
```
Login with `/api/auth/login`, copy `token`, then:
```bash
curl -X POST http://localhost:8080/api/jobs -H 'Authorization: Bearer TOKEN' -H 'Content-Type: application/json' -d '{"jobType":"UPPERCASE","payload":"hello distributed world","priority":8}'
```
Check `GET /api/jobs/{id}` or list with `GET /api/jobs`. Cancel with `DELETE /api/jobs/{id}`.

Supported demo job types:
- `ECHO`: returns payload unchanged.
- `UPPERCASE`: converts payload to uppercase.
- `SLEEP`: payload is milliseconds (0-120000), useful to observe concurrent workers.
- `FAIL`: intentionally fails so you can observe retry and dead-letter behavior.

## Architecture and delivery semantics
1. API transaction writes the job and an outbox event in PostgreSQL.
2. Scheduled outbox publisher sends the event to RabbitMQ and marks it published.
3. Worker claims the job with a conditional database update; duplicate deliveries cannot claim an already-running/finished job.
4. On success, the worker stores the result. On failure it sends a retry message through a TTL retry queue; after max attempts it routes a message to the DLQ and marks the job `DEAD_LETTERED`.
5. The outbox publisher is at-least-once: a crash after broker publish but before marking the event can cause duplicate delivery. The conditional job claim makes processing idempotent for this demo.

## Important limitations / production hardening
- The retry queue currently uses a fixed 10-second delay. For per-attempt exponential backoff, use tiered TTL queues or a delayed-message exchange plugin.
- The demo worker executes a small set of safe built-in handlers. Add a handler registry and strict payload schemas before accepting arbitrary tasks.
- The current worker claim is a simple conditional update. Add leases/heartbeats and recovery for jobs stuck in `RUNNING` after a worker crash.
- The rate limiter is a Redis fixed-window limiter by source IP. In production, consider a Lua-based atomic sliding window/token bucket and per-user quotas behind a trusted proxy.
- The JWT secret must be injected securely. Add refresh tokens, account lockout, email verification, RBAC and secret rotation before production use.
- RabbitMQ publisher confirms and mandatory routing should be enabled for stronger broker failure detection. Outbox polling should use row locking (`FOR UPDATE SKIP LOCKED`) when running multiple API replicas.
- Add integration tests using Testcontainers for PostgreSQL, Redis and RabbitMQ; the included test is only a smoke test.
- Priority applies to messages waiting in RabbitMQ; it cannot preempt a job already executing.

