# ADR 003: Optimistic Locking

## Context

Two concurrent transfers from the same account could both pass the overdraft validation check if they each read the pre-transaction balance, causing an overdraft violation. Database-level pessimistic locks (SELECT FOR UPDATE) prevent this but reduce throughput.

## Decision

Account entities use @Version for optimistic locking. On concurrent modification, Hibernate throws OptimisticLockException, which we catch and rethrow as a domain ConcurrentModificationException (HTTP 409 Conflict). Clients retry.

## Consequences

- ✓ No database-level locks - minimal lock contention, better throughput
- ✓ Scales well for low-to-medium contention scenarios
- ✗ Clients must implement retry logic with exponential backoff
  - For high-contention accounts (many concurrent transfers), retries increase latency
  - Alternative: pessimistic locking (simpler client logic but lower throughput)
