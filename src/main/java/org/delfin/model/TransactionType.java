package org.delfin.model;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transactiontype")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "typename")
    private String typeName;

    @Column(name = "calculationmethod")
    private String calculationMethod;

    public TransactionType() {
    }

    public TransactionType(String typeName, String calculationMethod) {
        this.typeName = typeName;
        this.calculationMethod = calculationMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

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

