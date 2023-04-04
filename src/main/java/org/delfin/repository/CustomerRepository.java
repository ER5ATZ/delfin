package org.delfin.repository;

import org.delfin.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
