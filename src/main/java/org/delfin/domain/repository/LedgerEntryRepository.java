package org.delfin.domain.repository;

import org.delfin.domain.model.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    Page<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);

    boolean existsByAccountIdAndIdempotencyKey(UUID accountId, String idempotencyKey);

    Optional<LedgerEntry> findByAccountIdAndIdempotencyKey(UUID accountId, String idempotencyKey);
}
