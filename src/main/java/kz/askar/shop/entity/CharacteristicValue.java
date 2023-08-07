package kz.askar.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "characteristics_values")
public class CharacteristicValue {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String value;

    @ManyToOne
    @JoinColumn(name = "characteristic_id")
    private Characteristic characteristic;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacteristicValue that = (CharacteristicValue) o;
        return Objects.equals(id, that.id) && Objects.equals(product, that.product) && Objects.equals(value, that.value) && Objects.equals(characteristic, that.characteristic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, value, characteristic);
    }
}
