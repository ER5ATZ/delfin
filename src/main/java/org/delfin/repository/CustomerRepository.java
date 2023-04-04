package org.delfin.repository;

import org.delfin.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@NoRepositoryBean
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
