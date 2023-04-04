package org.delfin.repository;

import org.delfin.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
}
