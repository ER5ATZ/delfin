package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "rolemappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleMappingEntity extends AbstractEntity {
    @Column
    private String userId;
    @Column
    private String roleName;
}
