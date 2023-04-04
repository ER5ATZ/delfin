package org.delfin.repository;

import org.delfin.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@NoRepositoryBean
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
}
