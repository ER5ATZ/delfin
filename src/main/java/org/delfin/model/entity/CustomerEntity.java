package org.delfin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractEntity {
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerMappingEntity> customerMappings = new ArrayList<>();

    @Override
    public String toString() {
        return "Customer{" +
                " id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", active=" + isActive() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                " }";
    }
}
