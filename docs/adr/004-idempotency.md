# ADR 004: Idempotency

## Context

Network failures can cause duplicate POST requests. A naive implementation would create duplicate ledger entries, violating business rules and audit integrity.

## Decision

All POST operations require an `Idempotency-Key` header (UUID recommended). A unique constraint on (account_id, idempotency_key) ensures duplicate requests return the existing entry with HTTP 200 instead of creating a new one.

## Consequences

- ✓ Safe to retry on timeout - same key always returns same result
- ✓ Audit trail remains accurate - no phantom duplicates
- ✗ Clients must generate and store unique keys per request
  - Key is scoped per-account, not global
  - Clients should use UUID.randomUUID() and correlate with request ID/trace ID
