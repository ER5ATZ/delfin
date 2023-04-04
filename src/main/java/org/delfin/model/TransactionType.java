package org.delfin.model;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transactiontype")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "typename")
    private String typeName;

    @Column(name = "calculationmethod")
    private String calculationMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionType that = (TransactionType) o;
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(calculationMethod, that.calculationMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, calculationMethod);
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", calculationMethod='" + calculationMethod + '\'' +
                '}';
    }
}


