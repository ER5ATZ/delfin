package org.delfin.repository;

import org.delfin.model.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface UserRepository extends JpaRepository<UserDetailsEntity, Long> {

    Optional<UserDetailsEntity> findByUsername(String username);

    Optional<UserDetailsEntity> findByEmail(String email);
}