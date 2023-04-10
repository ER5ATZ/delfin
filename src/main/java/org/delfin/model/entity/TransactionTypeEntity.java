package org.delfin.model.entity;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transactiontypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTypeEntity extends AbstractEntity {
    @Column(name = "typename")
    private String typeName;
    @Column(name = "calculation")
    private String calculation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionTypeEntity that = (TransactionTypeEntity) o;
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(calculation, that.calculation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, calculation);
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "id=" + getId() +
                ", typeName='" + getTypeName() +
                ", calculation='" + getCalculation() +
                '}';
    }
}


