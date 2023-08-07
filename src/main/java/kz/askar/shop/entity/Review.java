package kz.askar.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reviews")
public class Review {
     @Id
     @GeneratedValue(strategy =  GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;

     @ManyToOne
     @JoinColumn(name = "product_id")
     private Product product;

     private boolean status;
     private Integer rating;

     @Column(name = "review_text")
     private String reviewText;
     @Column(name = "review_date")
     private Timestamp reviewDate;


     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Review review = (Review) o;
          return status == review.status && Objects.equals(id, review.id) && Objects.equals(user, review.user) && Objects.equals(product, review.product) && Objects.equals(rating, review.rating) && Objects.equals(reviewText, review.reviewText) && Objects.equals(reviewDate, review.reviewDate);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, user, product, status, rating, reviewText, reviewDate);
     }
}
