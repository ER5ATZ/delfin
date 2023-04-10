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
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity extends AbstractEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;

    @Override
    public String toString() {
        return "Country{" +
                "id=" + getId() +
                ", balance=" + getName() +
                ", active=" + getCode() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                '}';
    }
}
