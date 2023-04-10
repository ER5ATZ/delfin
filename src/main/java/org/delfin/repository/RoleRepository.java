package org.delfin.repository;

import org.delfin.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByRolename(String Rolename);

}