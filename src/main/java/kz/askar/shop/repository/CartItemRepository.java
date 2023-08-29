package kz.askar.shop.repository;

import kz.askar.shop.entity.CartItem;
import kz.askar.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem ,Long> {

    List<CartItem> findByUser(User user);



}
