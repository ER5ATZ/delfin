package org.delfin.repository;

import org.delfin.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@NoRepositoryBean
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
}
