package kz.askar.shop.repository;

import kz.askar.shop.entity.Product;
import kz.askar.shop.entity.Review;
import kz.askar.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    Review findByUserAndProduct(User user, Product product);
}
