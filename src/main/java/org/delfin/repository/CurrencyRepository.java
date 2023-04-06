package org.delfin.repository;

import org.delfin.model.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
}
