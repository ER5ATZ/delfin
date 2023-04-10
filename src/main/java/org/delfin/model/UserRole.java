package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.model.entity.RoleEntity;
import org.delfin.model.entity.RoleMappingEntity;
import org.delfin.utils.StringUtils;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private String roleName;
    private String description;
    public UserRole(RoleEntity roleEntity) {
        this.roleName = roleEntity.getRoleName();
        String messageValue = readDescription(roleEntity.getRoleName());
        this.description = StringUtils.isEmpty(messageValue) ? roleEntity.getDescription() : messageValue;
    }

    public UserRole(RoleMappingEntity roleMappingEntity) {

    }

    private String readDescription(String name) {
        return System.getProperty("users.role." + name);
    }
}
