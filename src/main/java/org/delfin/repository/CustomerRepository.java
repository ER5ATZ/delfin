package org.delfin.repository;

import org.delfin.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
