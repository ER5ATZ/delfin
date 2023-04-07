package org.delfin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.delfin.model.entity.CustomerEntity;

import java.time.LocalDateTime;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private boolean active;
    private LocalDateTime created;

    public Customer(CustomerEntity customerEntity) {
        this.id = customerEntity.getId();
        this.firstName = customerEntity.getFirstName();
        this.lastName = customerEntity.getLastName();
        this.active = customerEntity.isActive();
        this.created = customerEntity.getCreated();
    }

    public CustomerEntity toEntity() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(this.id);
        customerEntity.setFirstName(this.firstName);
        customerEntity.setLastName(this.lastName);
        customerEntity.setActive(this.active);
        customerEntity.setCreated(this.created);
        return customerEntity;
    }
}

