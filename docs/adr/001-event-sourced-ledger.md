# ADR 001: Event-Sourced Ledger

## Context

Traditional banking applications store account balance as a mutable field in the account table, updating it on each transaction. This approach is simple but carries risks: stale balance bugs, race conditions during concurrent updates, and loss of audit trail.

## Decision

We derive account balance from an immutable ledger of entries (credits and debits). The balance is calculated at read-time: sum of all CREDIT entries minus sum of all DEBIT entries for the account. Account entities have no balance field.

## Consequences

- ✓ Audit trail is automatic - every transaction is an immutable entry
- ✓ No stale balance bugs - balance is always derived from true source
- ✓ Idempotency is natural - duplicate entries are rejected by unique constraint on (account_id, idempotency_key)
- ✗ Read performance is O(n) where n = number of entries
  - Acceptable for this showcase. Production deployments would add a materialized balance view or caching layer
