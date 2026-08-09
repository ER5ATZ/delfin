package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
    private Long userId;
    @Column
    private String roleName;
}
