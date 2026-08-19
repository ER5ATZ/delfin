package org.delfin.domain.repository;

import org.delfin.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("SELECT COALESCE(SUM(CASE WHEN e.type = org.delfin.domain.model.EntryType.CREDIT THEN e.amount.amount ELSE -e.amount.amount END), 0) FROM LedgerEntry e WHERE e.accountId = :accountId")
    BigDecimal sumBalance(@Param("accountId") UUID accountId);
}
