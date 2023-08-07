package kz.askar.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

     @Id
     @GeneratedValue(strategy =  GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     private String address;

     @Enumerated
     Status status;

     @Column(name = "order_date")
     private Timestamp orderDate;

     @OneToMany(mappedBy = "order" )
     private List<OrderedProduct> orderedProducts;


     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }





     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Order order = (Order) o;
          return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(address, order.address) && Objects.equals(orderDate, order.orderDate) && Objects.equals(orderedProducts, order.orderedProducts);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, user, address, orderDate, orderedProducts);
     }

     @Override
     public String toString() {
          return getClass().getSimpleName() + "(" +
                 "id = " + id + ", " +
                 "user = " + user + ", " +
                 "address = " + address + ", " +
                 "orderDate = " + orderDate + ")";
     }
}
