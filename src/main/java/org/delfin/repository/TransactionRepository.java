package org.delfin.repository;

import org.delfin.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@NoRepositoryBean
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
}
