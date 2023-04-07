package org.delfin.repository;

import org.delfin.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT a FROM AccountEntity a WHERE a.customer.id = ?1")
    List<AccountEntity> findByCustomerId(Long customerId);

    @Query("SELECT a FROM AccountEntity a WHERE a.id = ?1 AND a.active = true")
    Optional<AccountEntity> findActiveById(Long id);
}
