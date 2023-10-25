package kz.askar.shop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacteristicValue> characteristicValues;



    public Product(Long id, String name, Category category, List<Review> reviews, List<CharacteristicValue> characteristicValues) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.reviews = reviews;
        this.characteristicValues = characteristicValues;
    }

    public Product() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(reviews, product.reviews) && Objects.equals(characteristicValues, product.characteristicValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, reviews, characteristicValues);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
               "id = " + id + ", " +
               "name = " + name + ", " +
               "category = " + category + ")";
    }
}

