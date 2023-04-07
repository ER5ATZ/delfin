package org.delfin.repository;

import org.delfin.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Deprecated
    @Query("SELECT t FROM TransactionEntity t WHERE t.account.id = ?1 ORDER BY t.transactionTime DESC")
    List<TransactionEntity> findByAccountId(Long accountId);

    @Modifying
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM transaction t WHERE t.accountid = ?1 ORDER BY t.transactiontime DESC LIMIT ?2", nativeQuery = true)
    List<TransactionEntity> findByAccountId(Long accountId, int limit);
}
