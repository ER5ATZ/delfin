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
@Table(name = "currencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity extends AbstractEntity {
    @Column(nullable = false)
    private String code;
    @Column
    private String symbol;
    @Column
    private String description;

    @Override
    public String toString() {
        return "Customer{" +
                " id=" + getId() +
                ", code='" + getCode() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", symbol=" + getSymbol() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                " }";
    }
}
