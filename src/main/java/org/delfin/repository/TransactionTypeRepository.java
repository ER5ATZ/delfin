package org.delfin.repository;

import org.delfin.model.entity.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {
}
