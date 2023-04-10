package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.model.entity.RoleMappingEntity;
import org.delfin.model.entity.UserDetailsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private int failedLogins;
    private List<UserRole> roles = new ArrayList<>();

    public User(UserDetailsEntity entity) {
        this.username = entity.getUsername();
        this.password = "";
        this.email = entity.getEmail();
        this.failedLogins = entity.getFailedlogins();
        for (RoleMappingEntity roleMappingEntity : entity.getRoles()) {
            roles.add(new UserRole(roleMappingEntity));
        }
    }

    public UserDetailsEntity toEntity() {
        UserDetailsEntity entity = new UserDetailsEntity();
        entity.setUsername(getUsername());
        entity.setPassword(getPassword());
        entity.setEmail(getEmail());
        entity.setFailedlogins(getFailedLogins());
        return entity;
    }
}
