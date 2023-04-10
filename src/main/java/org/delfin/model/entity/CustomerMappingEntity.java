package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "customermappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMappingEntity extends AbstractEntity {
    @Column(name = "userid")
    private Long userId;

    @Column(name = "customerid")
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "id", insertable = false, updatable = false)
    private UserDetailsEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", referencedColumnName = "id", insertable = false, updatable = false)
    private CustomerEntity customer;
}
