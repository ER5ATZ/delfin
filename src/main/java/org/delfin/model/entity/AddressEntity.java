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
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity extends AbstractEntity {
    @Column(nullable = false)
    private String address;
    @Column
    private String suffix;
    @Column
    private String zip;
    @Column
    private String city;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", nullable = false)
    private CountryEntity country;

    @Override
    public String toString() {
        return "Address{" +
                " id=" + getId() +
                ", address='" + getAddress() +
                ", suffix='" + getSuffix() +
                ", zip=" + getZip() +
                ", city=" + getCity() +
                ", country=" + getCountry().getName() +
                ", created=" + getCreated() +
                ", updated=" + getUpdated() +
                " }";
    }
}
