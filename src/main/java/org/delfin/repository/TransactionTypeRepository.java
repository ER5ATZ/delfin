package org.delfin.repository;

import org.delfin.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
}
